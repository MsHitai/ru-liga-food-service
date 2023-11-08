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

    /**
     * Method accepts the order by its id and sends a message to the queue to change the status
     *
     * @param orderId is the id of an order
     */
    @Override
    public void acceptOrder(UUID orderId) {
        OrderActionDto dto = new OrderActionDto(orderId, OrderStatus.KITCHEN_ACCEPTED);
        String routingKey = "order.status";
        rabbitTemplate.convertAndSend("directExchange", routingKey, dto);
        // отправляем здесь повторно, пока нет промежуточного эндпоинта "начать готовить"
        OrderActionDto dto2 = new OrderActionDto(orderId, OrderStatus.KITCHEN_PREPARING);
        rabbitTemplate.convertAndSend("directExchange", routingKey, dto2);
    }

    /**
     * Method declines the order by its id and sends a message to the queue to change the status
     *
     * @param orderId is the id of an order
     */
    @Override
    public void denyOrder(UUID orderId) {
        OrderActionDto dto = new OrderActionDto(orderId, OrderStatus.KITCHEN_DENIED);
        String routingKey = "order.status";
        rabbitTemplate.convertAndSend("directExchange", routingKey, dto);
    }

    /**
     * Method sends two messages to two queues one to couriers and one to change the status
     *
     * @param orderId is the id of an order
     */
    @Override
    public void finishOrder(UUID orderId) {
        String routingKey = "courier.pick";
        rabbitTemplate.convertAndSend("directExchange", routingKey, orderId);
        //отправляем сообщение для смены статуса на "ожидающий доставку"
        OrderActionDto dto = new OrderActionDto(orderId, OrderStatus.DELIVERY_PENDING);
        String routingKey2 = "order.status";
        rabbitTemplate.convertAndSend("directExchange", routingKey2, dto);
    }
}
