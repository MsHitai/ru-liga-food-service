package ru.liga.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.liga.model.Order;

import java.util.List;
import java.util.Optional;

public interface OrderRepository extends JpaRepository<Order, Long> {

    @Query("select o from Order o left join fetch o.restaurant where o.id = :id")
    Optional<Order> findByIdWithRestaurant(Long id);

    @Query("select o from Order o left join fetch o.restaurant")
    List<Order> findAllOrdersWithRestaurants(Pageable page);
}
