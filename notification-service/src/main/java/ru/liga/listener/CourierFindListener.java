package ru.liga.listener;

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

    /**
     * Method notifies courier by id (currently only imitates)
     *
     * @param courierId identification of the closest available courier
     */
    @RabbitListener(queues = "courier2")
    public void notifyCouriers(Long courierId) {
        Courier courier = courierRepository.findById(courierId).orElseThrow(() ->
                new DataNotFoundException(String.format("Courier by id=%d is not in the database", courierId)));
        log.info("Sending an SMS to the courier by id " + courier.getId());
    }
}
