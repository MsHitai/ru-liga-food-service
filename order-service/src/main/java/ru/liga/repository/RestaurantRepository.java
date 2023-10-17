package ru.liga.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.liga.model.Restaurant;

public interface RestaurantRepository extends JpaRepository<Restaurant, Long> {
}
