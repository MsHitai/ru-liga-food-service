package ru.liga.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.liga.model.Courier;

@Repository
public interface CourierRepository extends JpaRepository<Courier, Long> {
}
