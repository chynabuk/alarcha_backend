package com.project.alarcha.repositories;

import com.project.alarcha.entities.RoomOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping
public interface RoomOrderRepository extends JpaRepository<RoomOrder, Long> {
}
