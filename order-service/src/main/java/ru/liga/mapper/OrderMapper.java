package ru.liga.mapper;

import lombok.experimental.UtilityClass;
import ru.liga.dto.OrderDto;
import ru.liga.dto.RestaurantDto;
import ru.liga.model.Order;

@UtilityClass
public class OrderMapper {

    public OrderDto mapToDto(Order order, RestaurantDto restaurantDto) {
        return OrderDto.builder()
                .id(order.getId())
                .restaurant(restaurantDto)
                .timestamp(order.getTimestamp())
                .build();
    }
}
