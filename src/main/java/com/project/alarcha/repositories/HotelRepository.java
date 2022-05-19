package com.project.alarcha.repositories;

import com.project.alarcha.entities.Hotel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HotelRepository extends JpaRepository<Hotel, Long> {
    @Query(nativeQuery = true, value = "SELECT * FROM hotels " +
            "WHERE NOT is_deleted")
    List<Hotel> getAll();
}