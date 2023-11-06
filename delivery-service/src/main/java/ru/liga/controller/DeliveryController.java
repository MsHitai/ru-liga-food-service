package ru.liga.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "Delivery")
@RequiredArgsConstructor
@RequestMapping("/deliveries")
@Slf4j
public class DeliveryController {

    private final DeliveryService deliveryService;

    @Operation(summary = "Получить все доставки, необходимо ввести статус заказа",
            security = @SecurityRequirement(name = "security_auth"))
    @GetMapping
    public List<DeliveryDto> findAllDeliveries(@RequestParam(name = "status") OrderStatus status,
                                               @PositiveOrZero @RequestParam(defaultValue = "0") Integer pageIndex,
                                               @Positive @RequestParam(defaultValue = "10") Integer pageCount) {
        log.info("Received GET request to find all deliveries with the state {}, from {} to {}", status, pageIndex,
                pageCount);
        return deliveryService.findAllDeliveries(status, pageIndex, pageCount);
    }

    @Operation(summary = "Обновить статус заказа, id заказа и статус приходят в теле запроса",
            security = @SecurityRequirement(name = "security_auth"))
    @PostMapping
    public void updateOrderStatus(@Valid @RequestBody OrderActionDto dto) {
        log.info("Received POST request to update order by id {} with action {}", dto.getId(), dto.getStatus());
        deliveryService.addDelivery(dto);
    }
}
