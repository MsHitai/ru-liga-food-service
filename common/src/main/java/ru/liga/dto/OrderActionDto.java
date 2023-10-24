package ru.liga.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import ru.liga.model.enums.OrderStatus;

import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
public class OrderActionDto {
    @NotNull
    private Long id;
    @NotNull
    private OrderStatus status;
}
