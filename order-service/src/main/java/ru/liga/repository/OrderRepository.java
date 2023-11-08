package ru.liga.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.liga.model.Order;
import ru.liga.model.enums.OrderStatus;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

    @Query("select o from Order o left join fetch o.restaurant where o.id = :id")
    Optional<Order> findByIdWithRestaurant(UUID id);

    @Query("select o from Order o left join fetch o.restaurant " +
            "where (:status is null or o.status = :status)")
    List<Order> findAllOrdersWithRestaurants(Pageable page, OrderStatus status);

    @Modifying
    @Query("update Order ord set ord.status = :status where ord.id = :id")
    void updateOrderStatus(OrderStatus status, UUID id);

    @Query("select o from Order o where o.id = :id")
    Optional<Order> findById(UUID id);
}
