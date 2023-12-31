package ru.liga.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.liga.dto.NewOrderDto;
import ru.liga.dto.OrderDto;
import ru.liga.dto.OrderToDeliverDto;
import ru.liga.model.enums.OrderStatus;
import ru.liga.service.OrderService;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/orders")
@Tag(name = "Orders")
@Slf4j
public class OrderController {

    private final OrderService orderService;

    @Operation(summary = "Найти все заказы с опциональным параметром статуса заказа",
            security = @SecurityRequirement(name = "security_auth"))
    @GetMapping
    public List<OrderDto> findAllOrders(@PositiveOrZero @RequestParam(defaultValue = "0") Integer pageIndex,
                                        @Positive @RequestParam(defaultValue = "10") Integer pageCount,
                                        @RequestParam(name = "status", required = false) OrderStatus status) {
        log.info("Received GET request to find all orders from page index {} to page count {} with status {}",
                pageIndex, pageCount, status);
        return orderService.findAllOrders(pageIndex, pageCount, status);
    }

    @Operation(summary = "Найти заказ по конкретному идентификатору orderId",
            security = @SecurityRequirement(name = "security_auth"))
    @GetMapping("/{orderId}")
    public OrderDto findOrderById(@PathVariable(name = "orderId") Long orderId) {
        log.info("Received GET request to find order by id {}", orderId);
        return orderService.findOrderById(orderId);
    }

    @Operation(summary = "Добавить новый заказ, необходимо указать id клиента",
            security = @SecurityRequirement(name = "security_auth"))
    @PostMapping
    public OrderToDeliverDto addOrder(@Valid @RequestBody NewOrderDto dto,
                                      @RequestParam(name = "customerId") Long customerId) {
        log.info("Received POST request to add an order {} from customer by id {}", dto.toString(), customerId);
        return orderService.addOrder(dto, customerId);
    }
}
