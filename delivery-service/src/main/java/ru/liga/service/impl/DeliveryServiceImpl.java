package ru.liga.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.liga.dto.*;
import ru.liga.mapper.CustomerMapper;
import ru.liga.mapper.RestaurantMapper;
import ru.liga.model.Courier;
import ru.liga.model.Customer;
import ru.liga.model.Order;
import ru.liga.model.Restaurant;
import ru.liga.model.enums.OrderStatus;
import ru.liga.repository.OrderRepository;
import ru.liga.service.DeliveryService;
import ru.liga.util.DistanceCalculator;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DeliveryServiceImpl implements DeliveryService {

    private final OrderRepository orderRepository;
    private final RabbitTemplate rabbitTemplate;

    /**
     * Method returns all deliveries with the chosen status with the set or default limit from pageIndex to pageCount
     *
     * @param status    an Order's status
     * @param pageIndex an offset
     * @param pageCount limit for a Pageable page
     * @return a List of deliveries
     */
    @Override
    @Transactional(readOnly = true)
    public List<DeliveryDto> findAllDeliveries(OrderStatus status, Integer pageIndex, Integer pageCount) {
        Pageable page = PageRequest.of(pageIndex / pageCount, pageCount);
        List<Order> orders = orderRepository.findAllByStatus(status, page);
        // собираем рестораны, клиентов и курьеров из заказов по идентификатору заказа
        Map<UUID, Restaurant> restaurants = orders.stream()
                .collect(Collectors.toMap(Order::getId, Order::getRestaurant));
        Map<UUID, Customer> customers = orders.stream()
                .collect(Collectors.toMap(Order::getId, Order::getCustomer));
        Map<UUID, Courier> couriers = orders.stream()
                .collect(Collectors.toMap(Order::getId, Order::getCourier));

        List<DeliveryDto> deliveries = new ArrayList<>();

        for (Order order : orders) {
            DeliveryDto dto = new DeliveryDto();
            dto.setOrderId(order.getId());
            Restaurant restaurant = restaurants.get(order.getId());
            Courier courier = couriers.get(order.getId());
            Customer customer = customers.get(order.getId());
            if (courier == null) {
                RestaurantDistanceDto restaurantDto = RestaurantMapper.mapToDto(restaurant.getAddress(), null);
                CustomerDto customerDto = CustomerMapper.mapToDto(customer.getAddress(), null);
                dto.setRestaurant(restaurantDto);
                dto.setCustomer(customerDto);
            } else {
                DistanceDto courierCoordinates = DistanceCalculator.parseCoordinates(courier.getCoordinates());
                DistanceDto destination = DistanceCalculator.parseCoordinates(customer.getCoordinates());
                double distance = DistanceCalculator.calculateDistance(courierCoordinates, destination);
                CustomerDto customerDto = CustomerMapper.mapToDto(customer.getAddress(), distance);
                dto.setCustomer(customerDto);

                DistanceDto restaurantCoordinates = DistanceCalculator.parseCoordinates(restaurant.getCoordinates());
                double distanceToRestaurant = DistanceCalculator.calculateDistance(courierCoordinates,
                        restaurantCoordinates);
                RestaurantDistanceDto restaurantDto = RestaurantMapper.mapToDto(restaurant.getAddress(),
                        distanceToRestaurant);
                dto.setRestaurant(restaurantDto);
            }
            deliveries.add(dto);
        }

        return deliveries;
    }

    /**
     * Method accepts the delivery and sends messages to change the order's status and to notification service
     *
     * @param orderId    the id of the order
     * @param customerId the id of a courier
     */
    @Override
    public void takeOrderForDelivery(UUID orderId, Long customerId) {
        OrderActionDto dto = new OrderActionDto(orderId, OrderStatus.DELIVERY_PICKING);
        // так как нет промежуточного эндпоинта отправляется два сообщения со сменой на два статуса подряд
        OrderActionDto dto2 = new OrderActionDto(orderId, OrderStatus.DELIVERY_DELIVERING);
        String routingKey = "order.status";
        rabbitTemplate.convertAndSend("directExchange", routingKey, dto);
        rabbitTemplate.convertAndSend("directExchange", routingKey, dto2);
        // также два подряд сообщения, так как нет промежуточного эндпоинта
        CustomerDeliveryDto customerDeliveryDto = new CustomerDeliveryDto(customerId, dto.getStatus());
        CustomerDeliveryDto customerDeliveryDto2 = new CustomerDeliveryDto(customerId, dto2.getStatus());
        rabbitTemplate.convertAndSend("directExchange", "customer.deliver", customerDeliveryDto);
        rabbitTemplate.convertAndSend("directExchange", "customer.deliver", customerDeliveryDto2);
    }

    /**
     * Method completes the delivery and sends a message to change the order's status and to notification service
     *
     * @param orderId    the id of the order
     * @param customerId the id of a courier
     */
    @Override
    public void completeDelivery(UUID orderId, Long customerId) {
        OrderActionDto dto = new OrderActionDto(orderId, OrderStatus.DELIVERY_COMPLETE);
        String routingKey = "order.status";
        rabbitTemplate.convertAndSend("directExchange", routingKey, dto);
        CustomerDeliveryDto customerDeliveryDto = new CustomerDeliveryDto(customerId, dto.getStatus());
        rabbitTemplate.convertAndSend("directExchange", "customer.deliver", customerDeliveryDto);
    }

}
