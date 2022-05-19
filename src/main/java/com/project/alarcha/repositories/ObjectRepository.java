package com.project.alarcha.repositories;

import com.project.alarcha.entities.Object;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ObjectRepository extends JpaRepository<Object, Long> {
    @Query(nativeQuery = true, value = "SELECT * FROM objects " +
            "WHERE NOT is_deleted")
    List<Object> getAll();
}
