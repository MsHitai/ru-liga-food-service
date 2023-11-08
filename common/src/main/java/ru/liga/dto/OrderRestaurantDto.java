package ru.liga.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderRestaurantDto implements Serializable {

    private UUID orderId;
    private Long restaurantId;
    private List<MenuItem> menuItems;
}
