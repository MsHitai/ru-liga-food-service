package ru.liga.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.liga.model.RestaurantMenuItem;

import java.util.List;

@Repository
public interface RestaurantMenuItemRepository extends JpaRepository<RestaurantMenuItem, Long> {

    List<RestaurantMenuItem> findAllByIdIn(List<Long> id);
}
