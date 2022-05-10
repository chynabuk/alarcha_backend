package com.project.alarcha.repositories;

import com.project.alarcha.entities.Hotel_Img;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface Hotel_ImgRepository extends JpaRepository<Hotel_Img, Long> {
}
