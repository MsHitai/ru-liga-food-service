package ru.liga.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;
import ru.liga.exception.DataNotFoundException;
import ru.liga.model.Courier;
import ru.liga.repository.CourierRepository;

@Service
@Slf4j
@RequiredArgsConstructor
public class CourierFindListener {

    private final CourierRepository courierRepository;

    @RabbitListener(queues = "courier2")
    public void notifyCouriers(String message) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        Long courierId = objectMapper.readValue(message, Long.class);
        Courier courier = courierRepository.findById(courierId).orElseThrow(() ->
                new DataNotFoundException(String.format("Courier by id=%d is not in the database", courierId)));
        log.info("Sending an SMS to the courier by id " + courier.getId());
    }
}
