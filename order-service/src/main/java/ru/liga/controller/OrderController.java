package ru.liga.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.liga.dto.NewOrderDto;
import ru.liga.dto.OrderDto;
import ru.liga.dto.OrderToDeliverDto;
import ru.liga.model.OrderStatus;
import ru.liga.service.OrderService;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
public class OrderController {

    private final OrderService orderService;

    @GetMapping("/orders")
    public List<OrderDto> findAllOrders(@PositiveOrZero @RequestParam Integer pageIndex,
                                        @Positive @RequestParam Integer pageCount,
                                        @RequestParam(name = "status", required = false) OrderStatus status) {
        log.info("Received GET request to find all orders from page index {} to page count {} with status {}",
                pageIndex, pageCount, status);
        return orderService.findAllOrders(pageIndex, pageCount, status);
    }

    @GetMapping("/order/{orderId}")
    public OrderDto findOrderById(@PathVariable(name = "orderId") Long orderId) {
        log.info("Received GET request to find order by id {}", orderId);
        return orderService.findOrderById(orderId);
    }

    @PostMapping("/order")
    public OrderToDeliverDto addOrder(@Valid @RequestBody NewOrderDto dto,
                                      @RequestParam(name = "customerId") Long customerId) {
        log.info("Received POST request to add an order {} from customer by id {}", dto.toString(), customerId);
        return orderService.addOrder(dto, customerId);
    }
}
