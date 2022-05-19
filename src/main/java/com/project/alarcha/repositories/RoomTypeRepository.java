package com.project.alarcha.repositories;

import com.project.alarcha.entities.RoomType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RoomTypeRepository extends JpaRepository<RoomType, Long> {
    @Query(nativeQuery = true, value = "SELECT * FROM room_types " +
            "WHERE NOT is_deleted")
    List<RoomType> getAll();
}
