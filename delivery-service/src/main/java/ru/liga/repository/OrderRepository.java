package ru.liga.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.liga.model.Order;
import ru.liga.model.enums.OrderStatus;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

    @Query("select o from Order o left join fetch o.customer where o.id = :id")
    Optional<Order> findByIdWithCustomer(UUID id);

    @Query("select o from Order o " +
            "left join fetch o.restaurant " +
            "left join fetch o.customer " +
            "left join fetch o.courier " +
            "where o.status = :status")
    List<Order> findAllByStatus(OrderStatus status, Pageable page);
}
