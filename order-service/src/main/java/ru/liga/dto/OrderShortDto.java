package ru.liga.dto;

import lombok.Data;

import java.util.List;

@Data
public class OrderShortDto {

    private Long id;
    private List<MenuItem> menuItems;
}
