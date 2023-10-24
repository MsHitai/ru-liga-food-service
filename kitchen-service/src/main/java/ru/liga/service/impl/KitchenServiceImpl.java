package ru.liga.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;
import ru.liga.clients.KitchenClient;
import ru.liga.dto.OrderActionDto;
import ru.liga.model.enums.OrderStatus;
import ru.liga.service.KitchenService;

@Service
@RequiredArgsConstructor
public class KitchenServiceImpl implements KitchenService {

    private final KitchenClient kitchenClient;
    private final RabbitTemplate rabbitTemplate;

    @Override
    public void acceptOrder(Long orderId) {
        OrderActionDto dto = new OrderActionDto(orderId, OrderStatus.KITCHEN_ACCEPTED);
        kitchenClient.updateOrderStatus(dto);
    }

    @Override
    public void denyOrder(Long orderId) {
        OrderActionDto dto = new OrderActionDto(orderId, OrderStatus.KITCHEN_DENIED);
        kitchenClient.updateOrderStatus(dto);
    }

    @Override
    public void finishOrder(Long orderId, String routingKey) {
        rabbitTemplate.convertAndSend("directExchange", routingKey, orderId);
    }
}
