package ru.liga.service;

import ru.liga.dto.DeliveryDto;
import ru.liga.model.enums.OrderStatus;

import java.util.List;
import java.util.UUID;

public interface DeliveryService {

    List<DeliveryDto> findAllDeliveries(OrderStatus status, Integer pageIndex, Integer pageCount);

    void takeOrderForDelivery(UUID orderId, Long courierId);

    void completeDelivery(UUID orderId, Long courierId);

}
