package ru.liga.service;

import ru.liga.dto.DeliveryDto;
import ru.liga.dto.OrderActionDto;
import ru.liga.model.Status;

import java.util.List;

public interface DeliveryService {
    List<DeliveryDto> findAllDeliveries(Status status);

    void addDelivery(Long deliveryId, OrderActionDto dto);

}
