package com.project.alarcha.repositories;

import com.project.alarcha.entities.Menu;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MenuRepository extends JpaRepository<Menu, Long> {
    @Query(nativeQuery = true, value = "SELECT * FROM menu " +
            "WHERE NOT is_deleted")
    List<Menu> getAll();
}
