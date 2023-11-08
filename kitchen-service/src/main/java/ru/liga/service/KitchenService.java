package ru.liga.service;

import java.util.UUID;

public interface KitchenService {
    void acceptOrder(UUID orderId);

    void denyOrder(UUID orderId);

    void finishOrder(UUID orderId);
}
