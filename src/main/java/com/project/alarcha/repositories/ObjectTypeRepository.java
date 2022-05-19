package com.project.alarcha.repositories;

import com.project.alarcha.entities.ObjectType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ObjectTypeRepository extends JpaRepository<ObjectType, Long> {
    @Query(nativeQuery = true, value = "SELECT * FROM object_types " +
            "WHERE NOT is_deleted")
    List<ObjectType> getAll();
}
