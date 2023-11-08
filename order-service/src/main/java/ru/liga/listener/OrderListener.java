package ru.liga.listener;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;
import ru.liga.dto.OrderActionDto;
import ru.liga.exception.DataNotFoundException;
import ru.liga.exception.OrderStatusException;
import ru.liga.model.Order;
import ru.liga.model.enums.OrderStatus;
import ru.liga.repository.OrderRepository;

@Service
@Slf4j
@RequiredArgsConstructor
public class OrderListener {

    private final OrderRepository orderRepository;
    private final RabbitTemplate rabbitTemplate;

    /**
     * Method updates the order status
     *
     * @param dto includes id of the order and its status
     */
    @RabbitListener(queues = "order")
    public void updateOrderStatus(OrderActionDto dto) {
        log.info("Received an update for the order by id {} with the status {}", dto.getId(), dto.getStatus());
        Order order = orderRepository.findById(dto.getId()).orElseThrow(() ->
                new DataNotFoundException(String.format("Order by id=%s is not in the database", dto.getId())));
        log.info("Found the order by id {}", order.getId());
        if (order.getStatus().equals(dto.getStatus())) {
            log.info("the status {} of the order has already been changed to this status {}", order.getStatus(),
                    dto.getStatus());
            throw new OrderStatusException("The order already has this status - " + dto.getStatus());
        }
        checkStatus(dto.getStatus(), order);
    }

    /**
     * Method checks whether to change the status
     *
     * @param statusToChange is the new status of the order
     * @param order          used to pass to the inner method
     */
    private void checkStatus(OrderStatus statusToChange, Order order) {
        switch (order.getStatus()) {
            case CUSTOMER_PAID:
                if (statusToChange.equals(OrderStatus.KITCHEN_ACCEPTED)) {
                    updateStatus(statusToChange, order);
                } else if (statusToChange.equals(OrderStatus.KITCHEN_DENIED)) {
                    updateStatus(statusToChange, order);
                }
                break;
            case KITCHEN_ACCEPTED:
                if (statusToChange.equals(OrderStatus.KITCHEN_PREPARING)) {
                    updateStatus(statusToChange, order);
                }
                break;
            case KITCHEN_DENIED:
                if (statusToChange.equals(OrderStatus.KITCHEN_REFUNDED)) {
                    updateStatus(statusToChange, order);
                }
                break;
            case KITCHEN_PREPARING:
                if (statusToChange.equals(OrderStatus.DELIVERY_PENDING)) {
                    orderRepository.updateOrderStatus(statusToChange, order.getId());
                }
                break;
            case DELIVERY_PENDING:
                if (statusToChange.equals(OrderStatus.DELIVERY_PICKING) ||
                        statusToChange.equals(OrderStatus.DELIVERY_DENIED)) {
                    updateStatus(statusToChange, order);
                }
                break;
            case DELIVERY_DENIED:
                if (statusToChange.equals(OrderStatus.DELIVERY_REFUNDED)) {
                    updateStatus(statusToChange, order);
                }
                break;
            case DELIVERY_PICKING:
                if (statusToChange.equals(OrderStatus.DELIVERY_DELIVERING) ||
                        statusToChange.equals(OrderStatus.DELIVERY_COMPLETE)) {
                    updateStatus(statusToChange, order);
                }
                break;
        }

    }

    /**
     * Method updates the order status and sends a message to the notification service
     *
     * @param statusToChange is the new status of the order
     * @param order          has the status of the order and can be saved with the updated status
     */
    private void updateStatus(OrderStatus statusToChange, Order order) {
        log.info("Changing status to {} and sending a message to notification", statusToChange);
        order.setStatus(statusToChange);
        orderRepository.save(order);
        rabbitTemplate.convertAndSend("directExchange", "status.update",
                "status updated to " + statusToChange);
    }
}
