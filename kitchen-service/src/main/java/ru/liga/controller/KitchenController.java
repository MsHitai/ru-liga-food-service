package ru.liga.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
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
}
