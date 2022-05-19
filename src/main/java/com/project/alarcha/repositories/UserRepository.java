package com.project.alarcha.repositories;

import com.project.alarcha.entities.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);

    @Query(nativeQuery = true, value = "SELECT * FROM users " +
            "WHERE NOT is_deleted AND ( role = 'ADMIN' OR role = 'CLIENT')")
    Page<User> getAll(PageRequest of);

    @Query(nativeQuery = true, value = "SELECT * FROM users " +
            "WHERE NOT is_deleted AND role = 'ADMIN'")
    List<User> getAdmins();
}