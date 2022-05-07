package com.project.alarcha.repositories;

import com.project.alarcha.entities.HotelHallOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface HotelHallOrderRepository extends JpaRepository<HotelHallOrder, Long> {
}
