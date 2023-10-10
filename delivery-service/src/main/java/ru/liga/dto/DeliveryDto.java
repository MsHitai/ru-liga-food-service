package ru.liga.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class DeliveryDto {

    private Long orderId;
    private RestaurantDto restaurant;
    private CustomerDto customer;
    private String payment;
}
