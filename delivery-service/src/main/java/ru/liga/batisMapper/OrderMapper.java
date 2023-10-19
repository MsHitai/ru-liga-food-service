package ru.liga.batisMapper;

import org.springframework.data.domain.Pageable;
import ru.liga.model.Order;
import ru.liga.model.OrderStatus;

import java.util.List;

public interface OrderMapper {

    List<Order> getOrderByStatus(OrderStatus status, Pageable page);

    void updateOrderStatus(OrderStatus status, Long id);
}
