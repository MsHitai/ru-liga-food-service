package ru.liga.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.liga.model.Courier;

public interface CourierRepository extends JpaRepository<Courier, Long> {
}
