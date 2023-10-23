package ru.liga.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.liga.dto.DeliveryDto;
import ru.liga.dto.OrderActionDto;
import ru.liga.model.enums.OrderStatus;
import ru.liga.service.DeliveryService;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/deliveries")
@Slf4j
public class DeliveryController {

    private final DeliveryService deliveryService;

    @GetMapping
    public List<DeliveryDto> findAllDeliveries(@RequestParam(name = "status") OrderStatus status,
                                               @PositiveOrZero @RequestParam(defaultValue = "0") Integer pageIndex,
                                               @Positive @RequestParam(defaultValue = "10") Integer pageCount) {
        log.info("Received GET request to find all deliveries with the state {}, from {} to {}", status, pageIndex,
                pageCount);
        return deliveryService.findAllDeliveries(status, pageIndex, pageCount);
    }

    @PostMapping
    public void updateOrderStatus(@Valid @RequestBody OrderActionDto dto) {
        log.info("Received POST request to update order by id {} with action {}", dto.getId(), dto.getStatus());
        deliveryService.addDelivery(dto);
    }
}
