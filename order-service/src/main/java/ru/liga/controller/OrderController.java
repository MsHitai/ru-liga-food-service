package ru.liga.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;
import ru.liga.dto.NewOrderDto;
import ru.liga.dto.OrderDto;
import ru.liga.dto.OrderToDeliverDto;
import ru.liga.model.Status;
import ru.liga.service.OrderService;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@RestController
@Slf4j
public class OrderController {

    private final OrderService orderService;

    @Autowired
    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @GetMapping("/orders")
    public List<OrderDto> findAllOrders(@PositiveOrZero @RequestParam(defaultValue = "0") Integer pageIndex,
                                        @Positive @RequestParam(defaultValue = "10") Integer pageCount,
                                        @RequestParam(name = "status", required = false) Status status) {
        log.info("Received GET request to find all orders from page index {} to page count {} with status {}",
                pageIndex, pageCount, status);
        Pageable page = PageRequest.of(pageIndex / pageCount, pageCount);
        return orderService.findAllOrders(page, status);
    }

    @GetMapping("/order/{orderId}")
    public OrderDto findOrderById(@PathVariable(name = "orderId") Long orderId) {
        log.info("Received GET request to find order by id {}", orderId);
        return orderService.findOrderById(orderId);
    }

    @PostMapping("/order")
    public OrderToDeliverDto addOrder(@Valid @RequestBody NewOrderDto dto) {
        log.info("Received POST request to add an order {}", dto.toString());
        return orderService.addOrder(dto);
    }
}
