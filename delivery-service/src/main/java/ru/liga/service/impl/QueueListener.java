package ru.liga.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;
import ru.liga.exception.DataNotFoundException;
import ru.liga.exception.DeliveryException;
import ru.liga.model.Courier;
import ru.liga.model.Order;
import ru.liga.model.enums.CourierStatus;
import ru.liga.repository.CourierRepository;
import ru.liga.repository.OrderRepository;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class QueueListener {

    private final CourierRepository courierRepository;
    private final OrderRepository orderRepository;

    @RabbitListener(queues = {"courier1", "courier2"})
    public void findCouriers(String message) throws JsonProcessingException {
        log.info("Looking for a courier for order by id " + message);
        ObjectMapper objectMapper = new ObjectMapper();
        Long orderId = objectMapper.readValue(message, Long.class);
        List<Courier> couriers = courierRepository.findAllByStatus(CourierStatus.INACTIVE);
        Order order = orderRepository.findById(orderId).orElseThrow(() ->
                new DataNotFoundException(String.format("Order by id=%d is not in the database", orderId)));
        if (couriers.size() > 0) {
            Courier courier = couriers.get(0);
            order.setCourier(courier);
            log.info("couriers have been found, setting a courier to the order by id " + orderId);
        } else {
            log.info("no couriers have been found, throwing an error message");
            throw new DeliveryException("All couriers are busy, please try again later");
        }
        orderRepository.save(order);
    }

}
