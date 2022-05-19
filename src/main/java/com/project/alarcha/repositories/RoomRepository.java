package com.project.alarcha.repositories;

import com.project.alarcha.entities.Room;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RoomRepository extends JpaRepository<Room, Long> {
    @Query(nativeQuery = true, value = "SELECT * FROM rooms " +
            "WHERE NOT is_deleted")
    List<Room> getAll();
}
