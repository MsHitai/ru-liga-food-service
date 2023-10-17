package ru.liga.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.liga.model.RestaurantMenuItem;

public interface RestaurantMenuItemRepository extends JpaRepository<RestaurantMenuItem, Long> {
}
