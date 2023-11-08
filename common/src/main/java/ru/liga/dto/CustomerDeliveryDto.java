package ru.liga.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import ru.liga.model.enums.OrderStatus;

import java.io.Serializable;

@Data
@AllArgsConstructor
public class CustomerDeliveryDto implements Serializable {

    private Long customerId;
    private OrderStatus status;
}
