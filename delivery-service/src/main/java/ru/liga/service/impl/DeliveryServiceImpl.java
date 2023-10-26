package ru.liga.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.liga.batisMapper.OrderMapper;
import ru.liga.dto.*;
import ru.liga.exception.DataNotFoundException;
import ru.liga.exception.ValidationException;
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
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DeliveryServiceImpl implements DeliveryService {

    private final OrderMapper orderMapper;
    private final OrderRepository orderRepository;

    /**
     * Method accepts a status and updates an order's status found by the id from dto body
     *
     * @param dto OrderActionDto that has an order's id and status
     */
    @Override
    @Transactional
    public void addDelivery(OrderActionDto dto) {
        checkOrderId(dto.getId());
        orderMapper.updateOrderStatus(dto.getStatus(), dto.getId());
    }

    /**
     * Method returns all deliveries with the chosen status with the set or default limit from pageIndex to pageCount
     *
     * @param status an Order's status
     * @param pageIndex an offset
     * @param pageCount limit for a Pageable page
     * @return a List of deliveries
     */
    @Override
    @Transactional(readOnly = true)
    public List<DeliveryDto> findAllDeliveries(OrderStatus status, Integer pageIndex, Integer pageCount) {
        Pageable page = PageRequest.of(pageIndex / pageCount, pageCount);
        List<Order> orders = orderMapper.getOrderByStatus(status, page.getPageSize());
        // собираем рестораны, клиентов и курьеров из заказов по идентификатору заказа
        Map<Long, Restaurant> restaurants = orders.stream()
                .collect(Collectors.toMap(Order::getId, Order::getRestaurant));
        Map<Long, Customer> customers = orders.stream()
                .collect(Collectors.toMap(Order::getId, Order::getCustomer));
        Map<Long, Courier> couriers = orders.stream()
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
                DistanceDto courierCoordinates = parseCoordinates(courier.getCoordinates());
                DistanceDto destination = parseCoordinates(customer.getCoordinates());
                double distance = DistanceCalculator.calculateDistance(courierCoordinates, destination);
                CustomerDto customerDto = CustomerMapper.mapToDto(customer.getAddress(), distance);
                dto.setCustomer(customerDto);

                DistanceDto restaurantCoordinates = parseCoordinates(restaurant.getCoordinates());
                double distanceToRestaurant = DistanceCalculator.calculateDistance(courierCoordinates,
                        restaurantCoordinates);
                RestaurantDistanceDto restaurantDto = RestaurantMapper.mapToDto(restaurant.getAddress(), distanceToRestaurant);
                dto.setRestaurant(restaurantDto);
            }
            deliveries.add(dto);
        }

        return deliveries;
    }

    /**
     * private Method that parses the coordinates that are written in a String and that are divided by a comma
     *
     * @param coordinates a set of latitude and longitude
     * @return a DistanceDto with two fields (latitude and longitude)
     */
    private DistanceDto parseCoordinates(String coordinates) {
        String[] coord = coordinates.split(",");
        double lat;
        double lon;
        try {
            lat = Double.parseDouble(coord[0]);
            lon = Double.parseDouble(coord[1]);
        } catch (NumberFormatException e) {
            throw new ValidationException("The coordinates are in the wrong format");
        }
        return new DistanceDto(lat, lon);
    }

    /**
     * Method checks whether a certain order exists in the database by searching it by its identification
     *
     * @param id order's ident
     */
    private void checkOrderId(Long id) {
        orderRepository.findById(id).orElseThrow(() ->
                new DataNotFoundException(String.format("Order by id=%d is not in the database", id)));
    }
}
