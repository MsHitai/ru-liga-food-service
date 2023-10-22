package ru.liga.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.liga.clients.KitchenClient;
import ru.liga.dto.OrderActionDto;
import ru.liga.model.enums.OrderStatus;
import ru.liga.service.KitchenService;

@Service
@RequiredArgsConstructor
public class KitchenServiceImpl implements KitchenService {

    private final KitchenClient kitchenClient;

    @Override
    public void acceptOrder(Long orderId) {
        OrderActionDto dto = new OrderActionDto(orderId, OrderStatus.KITCHEN_ACCEPTED);
        kitchenClient.updateOrderStatus(dto);
    }
}
