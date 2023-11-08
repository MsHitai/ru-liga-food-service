package ru.liga.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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

    @PostMapping("/{orderId}/take")
    public void takeOrderForDelivery(@PathVariable UUID orderId,
                                     @RequestParam(name = "courierId") Long courierId) {
        log.info("Received POST request to take order by id {} for delivery from courier by id {}", orderId, courierId);
        deliveryService.takeOrderForDelivery(orderId, courierId);
    }

    @PostMapping("/{orderId}/complete")
    public void completeDelivery(@PathVariable UUID orderId,
                                 @RequestParam(name = "courierId") Long courierId) {
        log.info("Received POST request to complete order by id {} for delivery from courier by id {}",
                orderId, courierId);
        deliveryService.completeDelivery(orderId, courierId);
    }
}
