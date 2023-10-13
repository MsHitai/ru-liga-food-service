package ru.liga.service;

import ru.liga.dto.DeliveryDto;
import ru.liga.dto.OrderActionDto;
import ru.liga.model.Status;

import java.util.List;

public interface DeliveryService {


    void addDelivery(OrderActionDto dto);

    List<DeliveryDto> findAllDeliveries(Status status, Integer pageIndex, Integer pageCount);
}
