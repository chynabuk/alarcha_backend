package com.project.alarcha.repositories;

import com.project.alarcha.entities.Area;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AreaRepository extends JpaRepository<Area, Long> {
    @Query(nativeQuery = true, value = "SELECT * FROM areas " +
            "WHERE NOT is_deleted")
    List<Area> getAll();
}
