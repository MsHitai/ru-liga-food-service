package ru.liga.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderRestaurant {

    private UUID orderId;
    private Long restaurantId;
    private List<MenuItem> menuItems;
}
