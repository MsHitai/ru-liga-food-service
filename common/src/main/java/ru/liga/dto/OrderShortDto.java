package ru.liga.dto;

import lombok.Data;

import java.util.List;
import java.util.UUID;

@Data
public class OrderShortDto {

    private UUID id;
    private List<MenuItem> menuItems;
}
