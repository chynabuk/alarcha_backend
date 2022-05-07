package com.project.alarcha.repositories;

import com.project.alarcha.entities.MenuSection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MenuSectionRepository extends JpaRepository<MenuSection, Long> {
}
