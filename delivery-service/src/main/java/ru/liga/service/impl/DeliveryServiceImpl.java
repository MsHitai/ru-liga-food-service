package ru.liga.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.liga.batisMapper.OrderMapper;
import ru.liga.dto.CustomerDto;
import ru.liga.dto.DeliveryDto;
import ru.liga.dto.OrderActionDto;
import ru.liga.dto.RestaurantDistanceDto;
import ru.liga.exception.DataNotFoundException;
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
@Transactional
@RequiredArgsConstructor
public class DeliveryServiceImpl implements DeliveryService {

    private final OrderMapper orderMapper;
    private final OrderRepository orderRepository;

    @Override
    public void addDelivery(OrderActionDto dto) {
        checkOrderId(dto.getId());
        orderMapper.updateOrderStatus(dto.getStatus(), dto.getId());
    }

    @Override
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
                List<Double> courierCoordinates = parseCoordinates(courier.getCoordinates());
                List<Double> destination = parseCoordinates(customer.getAddress());
                double distance = DistanceCalculator.calculateDistance(courierCoordinates, destination);
                CustomerDto customerDto = CustomerMapper.mapToDto(customer.getAddress(), distance);
                dto.setCustomer(customerDto);

                List<Double> restaurantCoordinates = parseCoordinates(restaurant.getAddress());
                double distanceToRestaurant = DistanceCalculator.calculateDistance(courierCoordinates,
                        restaurantCoordinates);
                RestaurantDistanceDto restaurantDto = RestaurantMapper.mapToDto(restaurant.getAddress(), distanceToRestaurant);
                dto.setRestaurant(restaurantDto);
            }
            deliveries.add(dto);
        }

        return deliveries;
    }

    private List<Double> parseCoordinates(String coordinates) {
        String[] coord = coordinates.split(",");
        double lat = Double.parseDouble(coord[0]);
        double lon = Double.parseDouble(coord[1]);
        return List.of(lat, lon);
    }

    private void checkOrderId(Long id) {
        orderRepository.findById(id).orElseThrow(() ->
                new DataNotFoundException(String.format("Order by id=%d is not in the database", id)));
    }
}
