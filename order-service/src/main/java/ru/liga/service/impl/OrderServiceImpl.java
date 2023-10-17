package ru.liga.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.liga.dto.ItemDto;
import ru.liga.dto.NewOrderDto;
import ru.liga.dto.OrderDto;
import ru.liga.dto.OrderToDeliverDto;
import ru.liga.mapper.MenuItemMapper;
import ru.liga.mapper.OrderMapper;
import ru.liga.mapper.RestaurantMapper;
import ru.liga.model.Order;
import ru.liga.model.OrderItem;
import ru.liga.model.OrderStatus;
import ru.liga.repository.OrderItemRepository;
import ru.liga.repository.OrderRepository;
import ru.liga.service.OrderService;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;

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
        return null;
    }

    @Override
    public OrderToDeliverDto addOrder(NewOrderDto dto) {
        return null;
    }

}
