package ru.liga.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import ru.liga.dto.DeliveryDto;
import ru.liga.model.enums.OrderStatus;
import ru.liga.service.DeliveryService;

import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;
import java.util.UUID;

@RestController
@Tag(name = "Delivery")
@RequiredArgsConstructor
@RequestMapping("/delivery")
@Slf4j
public class DeliveryController {

    private final DeliveryService deliveryService;

    @Operation(summary = "Получить все доставки, можно ввести статус заказа, по умолчанию \"DELIVERY_PENDING\"")
    @GetMapping
    public List<DeliveryDto> findAllDeliveries(@RequestParam(name = "status", defaultValue = "DELIVERY_PENDING")
                                               OrderStatus status,
                                               @PositiveOrZero @RequestParam(defaultValue = "0") Integer pageIndex,
                                               @Positive @RequestParam(defaultValue = "10") Integer pageCount) {
        log.info("Received GET request to find all deliveries with the state {}, from {} to {}", status, pageIndex,
                pageCount);
        return deliveryService.findAllDeliveries(status, pageIndex, pageCount);
    }

    @Operation(summary = "Принять заказ по id в доставку, необходимо ввести id клиента для отправки уведомления")
    @PostMapping("/{orderId}/take")
    @PreAuthorize("hasRole('ROLE_COURIER')")
    public void takeOrderForDelivery(@PathVariable UUID orderId,
                                     @RequestParam(name = "customerId") Long customerId) {
        log.info("Received POST request to take order by id {} for delivery to customer by id {}", orderId, customerId);
        deliveryService.takeOrderForDelivery(orderId, customerId);
    }

    @Operation(summary = "Доставить заказ по id, необходимо ввести id клиента для отправки уведомления")
    @PostMapping("/{orderId}/complete")
    @PreAuthorize("hasRole('ROLE_COURIER')")
    public void completeDelivery(@PathVariable UUID orderId,
                                 @RequestParam(name = "customerId") Long customerId) {
        log.info("Received POST request to complete order by id {} for delivery to customer by id {}",
                orderId, customerId);
        deliveryService.completeDelivery(orderId, customerId);
    }
}
