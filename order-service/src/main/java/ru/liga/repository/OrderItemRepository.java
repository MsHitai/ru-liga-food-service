package ru.liga.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.liga.model.OrderItem;

import java.util.List;
import java.util.Optional;

@Repository
public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {

    @Query("select oi from OrderItem oi left join fetch oi.order left join fetch oi.menuItem")
    List<OrderItem> findAllOrderItems(Pageable page);

    @Query("select oi from OrderItem oi left join fetch oi.order left join fetch oi.menuItem where oi.id = :id")
    Optional<OrderItem> findByIdWithItem(Long id);
}
