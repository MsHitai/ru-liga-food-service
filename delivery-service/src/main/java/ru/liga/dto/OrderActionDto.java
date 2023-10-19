package ru.liga.dto;

import lombok.Data;
import ru.liga.model.OrderStatus;

import javax.validation.constraints.NotNull;

@Data
public class OrderActionDto {
    @NotNull
    private Long id;
    @NotNull
    private OrderStatus status;
}
