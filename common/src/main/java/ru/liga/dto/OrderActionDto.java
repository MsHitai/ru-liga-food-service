package ru.liga.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import ru.liga.model.enums.OrderStatus;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.UUID;

@Data
@AllArgsConstructor
public class OrderActionDto implements Serializable {
    @NotNull
    private UUID id;
    @NotNull
    private OrderStatus status;
}
