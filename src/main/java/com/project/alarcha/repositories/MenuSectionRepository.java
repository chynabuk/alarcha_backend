package com.project.alarcha.repositories;

import com.project.alarcha.entities.MenuSection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MenuSectionRepository extends JpaRepository<MenuSection, Long> {
    @Query(nativeQuery = true, value = "SELECT * FROM menu_sections " +
            "WHERE NOT is_deleted")
    List<MenuSection> getAll();
}
