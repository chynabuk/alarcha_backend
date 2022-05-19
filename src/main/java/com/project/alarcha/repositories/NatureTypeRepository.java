package com.project.alarcha.repositories;

import com.project.alarcha.entities.NatureType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NatureTypeRepository extends JpaRepository<NatureType, Long> {
    @Query(nativeQuery = true, value = "SELECT * FROM nature_type " +
            "WHERE NOT is_deleted")
    List<NatureType> getAll();
}
