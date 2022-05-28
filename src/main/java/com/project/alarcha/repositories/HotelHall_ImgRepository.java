package com.project.alarcha.repositories;

import com.project.alarcha.entities.HotelHall_IMG;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface HotelHall_ImgRepository extends JpaRepository<HotelHall_IMG, Long> {
}
