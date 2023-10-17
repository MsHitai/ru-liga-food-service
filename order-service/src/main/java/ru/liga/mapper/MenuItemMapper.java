package ru.liga.mapper;

import lombok.experimental.UtilityClass;
import ru.liga.dto.ItemDto;
import ru.liga.model.RestaurantMenuItem;

import java.math.BigDecimal;

@UtilityClass
public class MenuItemMapper {

    public ItemDto mapToDto(RestaurantMenuItem item, int quantity) {
        BigDecimal price = item.getPrice();
        return ItemDto.builder()
                .price(price.doubleValue())
                .description(item.getDescription())
                .image(item.getImage())
                .quantity(quantity)
                .build();
    }
}
