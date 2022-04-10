package com.project.alarcha.repositories;

import com.project.alarcha.entities.HotelHall;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface HotelHallsRepository extends JpaRepository<HotelHall, Long> {
}
