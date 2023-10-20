package ru.liga.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.liga.model.Customer;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {
}
