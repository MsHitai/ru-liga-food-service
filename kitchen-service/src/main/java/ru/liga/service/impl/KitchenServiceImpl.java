package ru.liga.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;
import ru.liga.dto.OrderActionDto;
import ru.liga.model.enums.OrderStatus;
import ru.liga.service.KitchenService;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class KitchenServiceImpl implements KitchenService {

    private final RabbitTemplate rabbitTemplate;

    @Override
    public void acceptOrder(UUID orderId) {
        OrderActionDto dto = new OrderActionDto(orderId, OrderStatus.KITCHEN_ACCEPTED);
        String routingKey = "order.status";
        rabbitTemplate.convertAndSend("directExchange", routingKey, dto);
    }

    @Override
    public void denyOrder(UUID orderId) {
        OrderActionDto dto = new OrderActionDto(orderId, OrderStatus.KITCHEN_DENIED);
        String routingKey = "order.status";
        rabbitTemplate.convertAndSend("directExchange", routingKey, dto);
    }

    @Override
    public void finishOrder(UUID orderId) {
        String routingKey = "courier.pick";
        rabbitTemplate.convertAndSend("directExchange", routingKey, orderId);
    }
}
