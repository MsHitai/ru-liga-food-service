package ru.liga.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.liga.model.RestaurantMenuItem;

import java.util.List;

public interface RestaurantMenuItemRepository extends JpaRepository<RestaurantMenuItem, Long> {

    List<RestaurantMenuItem> findAllByIdIn(List<Long> id);
}
