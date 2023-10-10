package ru.liga.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class OrderShortDto {

    private Long id;
    @JsonProperty(namespace = "menu_items")
    private List<MenuItem> menuItems;
}
