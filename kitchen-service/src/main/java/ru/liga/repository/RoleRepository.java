package ru.liga.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.liga.model.Authority;

import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Authority, Long> {

    @Query("select a from Authority a join fetch a.user where a.user.username = :username")
    Optional<Authority> findByUsername(String username);
}
