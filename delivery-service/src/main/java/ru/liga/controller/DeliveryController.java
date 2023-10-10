package ru.liga.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.liga.dto.DeliveryDto;
import ru.liga.dto.OrderActionDto;
import ru.liga.model.Status;
import ru.liga.service.DeliveryService;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@RestController
@Slf4j
public class DeliveryController {

    private final DeliveryService deliveryService;

    @Autowired
    public DeliveryController(DeliveryService deliveryService) {
        this.deliveryService = deliveryService;
    }

    @GetMapping("/deliveries")
    public List<DeliveryDto> findAllDeliveries(@RequestParam(name = "status") Status status,
                                               @PositiveOrZero @RequestParam(defaultValue = "0") Integer pageIndex,
                                               @Positive @RequestParam(defaultValue = "10") Integer pageCount) {
        log.info("Received GET request to find all deliveries with the state {}, from {} to {}", status, pageIndex,
                pageCount);
        return deliveryService.findAllDeliveries(status);
    }

    @PostMapping("/delivery/{id}")
    public void addDelivery(@PathVariable(name = "id") Long deliveryId,
                            @Valid @RequestBody OrderActionDto dto) {
        log.info("Received POST request to add delivery by id {} with action {}", deliveryId, dto.toString());
        deliveryService.addDelivery(deliveryId, dto);
    }
}
