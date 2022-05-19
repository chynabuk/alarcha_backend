package com.project.alarcha.repositories;

import com.project.alarcha.entities.HotelHall;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HotelHallsRepository extends JpaRepository<HotelHall, Long> {
    @Query(nativeQuery = true, value = "SELECT * FROM hotel_halls " +
            "WHERE NOT is_deleted")
    List<HotelHall> getAll();
}
