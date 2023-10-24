package ru.liga.mapper;

import lombok.experimental.UtilityClass;
import ru.liga.dto.RestaurantDto;

@UtilityClass
public class RestaurantMapper {

    public RestaurantDto mapToDto(String name) {
        return new RestaurantDto(name);
    }
}
