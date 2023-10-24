package ru.liga.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.liga.service.KitchenService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/kitchen")
@Slf4j
public class KitchenController {

    private final KitchenService kitchenService;

    @PostMapping("/accept/{orderId}")
    public void acceptOrder(@PathVariable Long orderId) {
        log.info("Received POST request to accept order by id {}", orderId);
        kitchenService.acceptOrder(orderId);
    }

    @PostMapping("/deny/{orderId}")
    public void denyOrder(@PathVariable Long orderId) {
        log.info("Received POST request to deny order by id {}", orderId);
        kitchenService.denyOrder(orderId);
    }

    @PostMapping("/finish/{orderId}")
    public void finishOrder(@PathVariable Long orderId, @RequestParam(name = "routingKey") String routingKey) {
        log.info("Received POST request to finish order by id {} to the routingKey {}", orderId, routingKey);
        kitchenService.finishOrder(orderId, routingKey);
    }
}