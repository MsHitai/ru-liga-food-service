package ru.liga.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

@Data
@AllArgsConstructor
@Builder
public class NewOrderDto {

    @NotNull
    private Long restaurantId;
    @NotEmpty
    @NotNull
    @JsonProperty(namespace = "menu_items")
    private List<MenuItem> menuItems;
}
