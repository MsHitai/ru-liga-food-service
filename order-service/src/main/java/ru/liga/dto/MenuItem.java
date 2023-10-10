package ru.liga.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class MenuItem {

    private int quantity;
    @JsonProperty(namespace = "menu_item_id")
    private Long menuItemId;
}
