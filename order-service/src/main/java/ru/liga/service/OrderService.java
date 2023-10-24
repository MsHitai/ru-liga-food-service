package ru.liga.service;

import ru.liga.dto.NewOrderDto;
import ru.liga.dto.OrderDto;
import ru.liga.dto.OrderToDeliverDto;
import ru.liga.model.enums.OrderStatus;

import java.util.List;

public interface OrderService {

    List<OrderDto> findAllOrders(Integer pageIndex, Integer pageCount, OrderStatus status);

    OrderDto findOrderById(Long orderId);

    OrderToDeliverDto addOrder(NewOrderDto dto, Long customerId);
}
