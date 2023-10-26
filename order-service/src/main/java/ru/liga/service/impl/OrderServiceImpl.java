package ru.liga.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.liga.dto.*;
import ru.liga.exception.DataNotFoundException;
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
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;
    private final CustomerRepository customerRepository;
    private final RestaurantRepository restaurantRepository;
    private final RestaurantMenuItemRepository menuItemRepository;

    /**
     * Method returns all orders from pageIndex to pageCount
     *
     * @param pageIndex the offset
     * @param pageCount the limit for a Pageable page
     * @param status the Order status
     * @return a List of Orders
     */
    @Override
    @Transactional(readOnly = true)
    public List<OrderDto> findAllOrders(Integer pageIndex, Integer pageCount, OrderStatus status) {
        Pageable page = PageRequest.of(pageIndex / pageCount, pageCount);
        List<Order> orders = orderRepository.findAllOrdersWithRestaurants(page);
        List<OrderDto> orderDtos = orders.stream()
                .map(order -> OrderMapper.mapToDto(order, RestaurantMapper.mapToDto(order.getRestaurant().getName())))
                .collect(Collectors.toList());

        List<OrderItem> orderItems = orderItemRepository.findAllOrderItems(page);
        Map<Long, List<ItemDto>> itemDtos = orderItems.stream()
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
    public OrderDto findOrderById(Long orderId) {
        Order order = orderRepository.findByIdWithRestaurant(orderId).orElseThrow(() ->
                new DataNotFoundException(String.format("Order by id=%d is not in the database", orderId)));
        OrderItem orderItem = orderItemRepository.findByIdWithItem(orderId).orElseThrow(() ->
                new DataNotFoundException(String.format("OrderItem by id=%d is not in the database", orderId)));
        OrderDto dto = OrderMapper.mapToDto(order, RestaurantMapper.mapToDto(order.getRestaurant().getName()));
        dto.setItems(List.of(MenuItemMapper.mapToDto(orderItem.getMenuItem(), orderItem.getQuantity())));
        return dto;
    }

    /**
     * Method accepts a new order dto and a customer id who creates the order
     *
     * @param dto a NewOrderDto
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
        result.setEstimatedArrival(LocalDateTime.now().plusDays(3));

        return result;
    }

}
