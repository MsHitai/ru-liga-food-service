package ru.liga.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.liga.service.KitchenService;

import java.util.UUID;

@RestController
@Tag(name = "Kitchen")
@RequiredArgsConstructor
@RequestMapping("/kitchen")
@Slf4j
public class KitchenController {

    private final KitchenService kitchenService;

    @Operation(summary = "Принять заказ, необходимо указать id заказа")
    @PostMapping("/{orderId}/accept")
    public void acceptOrder(@PathVariable UUID orderId) {
        log.info("Received POST request to accept order by id {}", orderId);
        kitchenService.acceptOrder(orderId);
    }

    @Operation(summary = "Отклонить заказ, необходимо указать id заказа")
    @PostMapping("/{orderId}/decline")
    public void denyOrder(@PathVariable UUID orderId) {
        log.info("Received POST request to deny order by id {}", orderId);
        kitchenService.denyOrder(orderId);
    }

    @Operation(summary = "Завершить заказ, необходимо указать id заказа")
    @PostMapping("/{orderId}/ready")
    public void finishOrder(@PathVariable UUID orderId) {
        log.info("Received POST request to finish order by id {}", orderId);
        kitchenService.finishOrder(orderId);
    }
}