package ru.liga.listener;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;
import ru.liga.dto.DistanceDto;
import ru.liga.exception.DataNotFoundException;
import ru.liga.exception.DeliveryException;
import ru.liga.model.Courier;
import ru.liga.model.Customer;
import ru.liga.model.Order;
import ru.liga.model.enums.CourierStatus;
import ru.liga.repository.CourierRepository;
import ru.liga.repository.OrderRepository;
import ru.liga.util.DistanceCalculator;

import java.util.Comparator;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class CourierPickListener {

    private final CourierRepository courierRepository;
    private final OrderRepository orderRepository;
    private final RabbitTemplate rabbitTemplate;

    /**
     * Method searches available couriers in the database
     *
     * @param orderId identification of the finished order
     */
    @RabbitListener(queues = "courier1")
    public void findCouriers(UUID orderId) {
        log.info("Looking for a courier for order by id " + orderId);
        Order order = orderRepository.findByIdWithCustomer(orderId).orElseThrow(() ->
                new DataNotFoundException(String.format("Order by id=%s is not in the database", orderId)));
        if (order.getCourier() != null) {
            rabbitTemplate.convertAndSend("directExchange", "courier.find",
                    order.getCourier().getId());
        } else {
            lookForCouriers(order);
        }
        orderRepository.save(order);
    }

    /**
     * Method checks if there are couriers with the status 'INACTIVE' and then uses DistanceCalculator to find the
     * closest courier to the customer's coordinates, although the distance between the restaurant and the
     * position of the courier is not evaluated
     *
     * @param order has the connection to the customer, whose coordinates are used to find the closest courier
     */
    private void lookForCouriers(Order order) {
        List<Courier> couriers = courierRepository.findAllByStatus(CourierStatus.INACTIVE);
        if (!couriers.isEmpty()) {
            Customer customer = order.getCustomer();//не проверяем на null, так как на уровне базы - not null constraint
            DistanceDto customerDistance = DistanceCalculator.parseCoordinates(customer.getCoordinates());
            Courier closestCourier = couriers.stream()
                    .sorted(Comparator.comparingDouble(courier -> DistanceCalculator.calculateDistance(
                            DistanceCalculator.parseCoordinates(courier.getCoordinates()), customerDistance)))
                    .collect(Collectors.toList()).get(0);
            order.setCourier(closestCourier);
            log.info("couriers have been found, setting a courier to the order by id " + order.getId());
            rabbitTemplate.convertAndSend("directExchange", "courier.find", closestCourier.getId());
        } else {
            log.info("no couriers have been found, throwing an error message");
            throw new DeliveryException("All couriers are busy, please try again later");
        }
    }

}
