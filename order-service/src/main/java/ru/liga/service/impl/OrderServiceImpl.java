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
import ru.liga.repository.*;
import ru.liga.service.OrderService;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;
    private final CustomerRepository customerRepository;
    private final RestaurantRepository restaurantRepository;
    private final RestaurantMenuItemRepository menuItemRepository;


    @Override
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

    @Override
    public OrderDto findOrderById(Long orderId) {
        Order order = orderRepository.findByIdWithRestaurant(orderId).orElseThrow(() ->
                new DataNotFoundException(String.format("Order by id=%d is not in the database", orderId)));
        OrderItem orderItem = orderItemRepository.findByIdWithItem(orderId).orElseThrow(() ->
                new DataNotFoundException(String.format("OrderItem by id=%d is not in the database", orderId)));
        OrderDto dto = OrderMapper.mapToDto(order, RestaurantMapper.mapToDto(order.getRestaurant().getName()));
        dto.setItems(List.of(MenuItemMapper.mapToDto(orderItem.getMenuItem(), orderItem.getQuantity())));
        return dto;
    }

    @Override
    public OrderToDeliverDto addOrder(NewOrderDto dto, Long customerId) {
        Order order = new Order();
        Customer customer = customerRepository.findById(customerId).orElseThrow(() ->
                new DataNotFoundException(String.format("Customer by id=%d is not in the database", customerId)));
        Restaurant restaurant = restaurantRepository.findById(dto.getRestaurantId()).orElseThrow(() ->
                new DataNotFoundException(String.format("Customer by id=%d is not in the database",
                        dto.getRestaurantId())));
        order.setCustomer(customer);
        order.setRestaurant(restaurant);
        order.setStatus(OrderStatus.PENDING);
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

        orderRepository.save(order);
        orderItemRepository.saveAll(orderItems);

        OrderToDeliverDto result = new OrderToDeliverDto();
        result.setId(order.getId());
        result.setEstimatedArrival(LocalDateTime.now().plusDays(3));

        return result;
    }

}
