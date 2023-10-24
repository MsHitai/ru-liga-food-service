package ru.liga.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DeliveryDto {

    private Long orderId;
    private RestaurantDto restaurant;
    private CustomerDto customer;
    private BigDecimal payment;
}
