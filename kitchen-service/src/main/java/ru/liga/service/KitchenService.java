package ru.liga.service;

public interface KitchenService {
    void acceptOrder(Long orderId);

    void denyOrder(Long orderId);

    void finishOrder(Long orderId, String routingKey);
}
