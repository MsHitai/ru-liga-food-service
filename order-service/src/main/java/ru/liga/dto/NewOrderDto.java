package ru.liga.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.List;

@Data
@AllArgsConstructor
public class NewOrderDto {

    @NotNull
    private Long restaurantId;

    @NotNull
    private List<MenuItem> menuItems;
}
