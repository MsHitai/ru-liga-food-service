package ru.liga.service.impl;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.liga.dto.DeliveryDto;
import ru.liga.dto.OrderActionDto;
import ru.liga.model.Status;
import ru.liga.service.DeliveryService;

import java.util.List;

@Service
public class DeliveryServiceImpl implements DeliveryService {


    @Override
    public void addDelivery(OrderActionDto dto) {
    }

    @Override
    public List<DeliveryDto> findAllDeliveries(Status status, Integer pageIndex, Integer pageCount) {
        Pageable page = PageRequest.of(pageIndex / pageCount, pageCount);
        return null;
    }
}
