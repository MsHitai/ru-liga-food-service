package ru.liga.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class RestaurantDistanceDto {

    private String address;
    private Double distance;
}
