package ru.liga.service.impl;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.liga.dto.NewOrderDto;
import ru.liga.dto.OrderDto;
import ru.liga.dto.OrderToDeliverDto;
import ru.liga.model.Status;
import ru.liga.service.OrderService;

import java.util.List;

@Service
public class OrderServiceImpl implements OrderService {


    @Override
    public List<OrderDto> findAllOrders(Integer pageIndex, Integer pageCount, Status status) {
        Pageable page = PageRequest.of(pageIndex / pageCount, pageCount);
        return null;
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
