package ru.liga.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.liga.dto.*;
import ru.liga.exception.DataNotFoundException;
import ru.liga.exception.OrderStatusException;
import ru.liga.mapper.MenuItemMapper;
import ru.liga.mapper.OrderMapper;
import ru.liga.mapper.RestaurantMapper;
import ru.liga.model.*;
import ru.liga.model.enums.OrderStatus;
import ru.liga.repository.*;
import ru.liga.service.OrderService;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;
    private final CustomerRepository customerRepository;
    private final RestaurantRepository restaurantRepository;
    private final RestaurantMenuItemRepository menuItemRepository;
    private final RabbitTemplate rabbitTemplate;

    /**
     * Method returns all orders from pageIndex to pageCount
     *
     * @param pageIndex the offset
     * @param pageCount the limit for a Pageable page
     * @param status    the Order status
     * @return a List of Orders
     */
    @Override
    @Transactional(readOnly = true)
    public List<OrderDto> findAllOrders(Integer pageIndex, Integer pageCount, OrderStatus status) {
        Pageable page = PageRequest.of(pageIndex / pageCount, pageCount);
        List<Order> orders = orderRepository.findAllOrdersWithRestaurants(page, status);
        List<OrderDto> orderDtos = orders.stream()
                .map(order -> OrderMapper.mapToDto(order, RestaurantMapper.mapToDto(order.getRestaurant().getName())))
                .collect(Collectors.toList());

        List<OrderItem> orderItems = orderItemRepository.findAllOrderItems(page);
        Map<UUID, List<ItemDto>> itemDtos = orderItems.stream()
                .collect(Collectors.groupingBy(orderItem -> orderItem.getOrder().getId(),
                        Collectors.mapping(item -> MenuItemMapper.mapToDto(item.getMenuItem(), item.getQuantity()),
                                Collectors.toList())));

        orderDtos.forEach(orderDto -> orderDto.setItems(itemDtos.get(orderDto.getId())));
        return orderDtos;
    }

    /**
     * Method returns an order by its identification
     *
     * @param orderId identification of an order
     * @return an order
     */
    @Override
    @Transactional(readOnly = true)
    public OrderDto findOrderById(UUID orderId) {
        Order order = orderRepository.findByIdWithRestaurant(orderId).orElseThrow(() ->
                new DataNotFoundException(String.format("Order by id=%s is not in the database", orderId)));
        OrderItem orderItem = orderItemRepository.findByIdWithItem(orderId).orElseThrow(() ->
                new DataNotFoundException(String.format("OrderItem by id=%s is not in the database", orderId)));
        OrderDto dto = OrderMapper.mapToDto(order, RestaurantMapper.mapToDto(order.getRestaurant().getName()));
        dto.setItems(List.of(MenuItemMapper.mapToDto(orderItem.getMenuItem(), orderItem.getQuantity())));
        return dto;
    }

    /**
     * Method accepts a new order dto and a customer id who creates the order
     * creates the order and sends to 'kitchen' queue that a new order is available
     *
     * @param dto        a NewOrderDto
     * @param customerId an identification of a customer
     * @return the created order
     */
    @Override
    @Transactional
    public OrderToDeliverDto addOrder(NewOrderDto dto, Long customerId) {
        Order order = new Order();
        Customer customer = customerRepository.findById(customerId).orElseThrow(() ->
                new DataNotFoundException(String.format("Customer by id=%d is not in the database", customerId)));
        Restaurant restaurant = restaurantRepository.findById(dto.getRestaurantId()).orElseThrow(() ->
                new DataNotFoundException(String.format("Restaurant by id=%d is not in the database",
                        dto.getRestaurantId())));
        order.setCustomer(customer);
        order.setRestaurant(restaurant);
        order.setStatus(OrderStatus.CUSTOMER_CREATED);
        //собираем количество блюд по идентификатору блюда
        Map<Long, Integer> itemsQuantity = dto.getMenuItems().stream()
                .collect(Collectors.toMap(MenuItem::getMenuItemId, MenuItem::getQuantity));

        List<Long> idsMenuItems = new ArrayList<>(itemsQuantity.keySet());
        List<RestaurantMenuItem> menuItems = menuItemRepository.findAllByIdIn(idsMenuItems);
        List<OrderItem> orderItems = new ArrayList<>();

        for (RestaurantMenuItem menuItem : menuItems) {
            OrderItem item = new OrderItem();
            item.setOrder(order);
            item.setMenuItem(menuItem);
            item.setPrice(menuItem.getPrice());
            item.setQuantity(itemsQuantity.get(menuItem.getId()));
            orderItems.add(item);
        }

        orderItemRepository.saveAll(orderItems);

        OrderToDeliverDto result = new OrderToDeliverDto();
        result.setId(order.getId());
        result.setEstimatedArrival(LocalDateTime.now().plusHours(1));

        OrderRestaurantDto orderRestaurantDto = new OrderRestaurantDto(order.getId(), dto.getRestaurantId(), dto.getMenuItems());

        rabbitTemplate.convertAndSend("directExchange", "kitchen.cook", orderRestaurantDto);

        return result;
    }

    /**
     * Method accepts a status and updates an order's status found by the id from dto body
     *
     * @param dto OrderActionDto that has an order's id and status
     */
    @Override
    @Transactional
    public void updateOrderStatus(OrderActionDto dto, UUID orderId) {
        Order order = checkOrderId(dto.getId());
        checkStatus(order, dto.getStatus());
    }

    /**
     * Method checks the order statuses and doesn't allow updating to any status at will
     *
     * @param order          to save in the repository
     * @param statusToUpdate is the status to which the order's status is to be updated
     */
    private void checkStatus(Order order, OrderStatus statusToUpdate) {
        if (order.getStatus().equals(statusToUpdate)) {
            throw new OrderStatusException("The order already has this status - " + statusToUpdate);
        }
        if (order.getStatus() == OrderStatus.CUSTOMER_CREATED) {
            if (statusToUpdate.equals(OrderStatus.CUSTOMER_PAID) ||
                    statusToUpdate.equals(OrderStatus.CUSTOMER_CANCELLED)) {
                orderRepository.updateOrderStatus(statusToUpdate, order.getId());
            }
        }
    }

    /**
     * Method checks whether a certain order exists in the database by searching it by its identification
     *
     * @param id order's identification
     */
    private Order checkOrderId(UUID id) {
        return orderRepository.findById(id).orElseThrow(() ->
                new DataNotFoundException(String.format("Order by id=%s is not in the database", id)));
    }

}
