package ru.liga.listener;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;
import ru.liga.dto.OrderRestaurant;
import ru.liga.exception.DataNotFoundException;
import ru.liga.model.Restaurant;
import ru.liga.repository.RestaurantRepository;

import java.time.LocalDateTime;

@Service
@Slf4j
@RequiredArgsConstructor
public class KitchenListener {

    private final RestaurantRepository restaurantRepository;

    /**
     * Method notifies (currently only imitates) the restaurant, providing the id of the new order and the menu items
     *
     * @param orderRestaurant includes the id of the order, the id of the restaurant and the menu items
     */
    @RabbitListener(queues = "kitchen1")
    public void notifyKitchenNewOrder(OrderRestaurant orderRestaurant) {
        log.info("Received a notification about a new order by id {} for the restaurant by id {}",
                orderRestaurant.getOrderId(), orderRestaurant.getRestaurantId());
        Restaurant restaurant = restaurantRepository.findById(orderRestaurant.getRestaurantId()).orElseThrow(() ->
                new DataNotFoundException(String.format("Restaurant by id=%d is not in the database",
                        orderRestaurant.getRestaurantId())));
        log.info("Found the restaurant by id {} in the database", restaurant.getId());
        log.info("Sending a notification to the restaurant providing the id of the order {} and the menu items {}",
                orderRestaurant.getOrderId(), orderRestaurant.getMenuItems());
    }

    /**
     * Method notifies (currently only imitates) the restaurant that the status of the order has been changed
     *
     * @param message includes the message from the order-service
     */
    @RabbitListener(queues = "kitchen2")
    public void notifyKitchenOrderStatus(String message) {
        log.info("Received the message {} from the order service at {} ", message, LocalDateTime.now());
        log.info("Sending a notification to the system that the order status has been changed");
    }
}
