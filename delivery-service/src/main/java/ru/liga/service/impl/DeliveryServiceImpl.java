package ru.liga.service.impl;

import org.springframework.stereotype.Service;
import ru.liga.dto.DeliveryDto;
import ru.liga.dto.OrderActionDto;
import ru.liga.model.Status;
import ru.liga.service.DeliveryService;

import java.util.List;

@Service
public class DeliveryServiceImpl implements DeliveryService {
    @Override
    public List<DeliveryDto> findAllDeliveries(Status status) {
        return null;
    }

    @Override
    public void addDelivery(Long deliveryId, OrderActionDto dto) {
    }
}
