package com.project.alarcha.repositories;


import com.project.alarcha.entities.Nature;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NatureRepository extends JpaRepository<Nature, Long> {
    @Query(nativeQuery = true, value = "SELECT * FROM nature " +
            "WHERE NOT is_deleted")
    List<Nature> getAll();
}
