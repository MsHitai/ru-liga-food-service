package ru.liga.service;

import org.springframework.data.domain.Pageable;
import ru.liga.dto.NewOrderDto;
import ru.liga.dto.OrderDto;
import ru.liga.dto.OrderToDeliverDto;
import ru.liga.model.Status;

import java.util.List;

public interface OrderService {
    List<OrderDto> findAllOrders(Pageable page, Status status);

    OrderDto findOrderById(Long orderId);

    OrderToDeliverDto addOrder(NewOrderDto dto);

}
