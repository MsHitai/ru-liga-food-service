package ru.liga.mapper;

import lombok.experimental.UtilityClass;
import ru.liga.dto.RestaurantDistanceDto;

@UtilityClass
public class RestaurantMapper {

    public RestaurantDistanceDto mapToDto(String address, Double distance) {
        return new RestaurantDistanceDto(address, distance);
    }
}
