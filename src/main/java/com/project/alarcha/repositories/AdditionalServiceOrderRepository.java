package com.project.alarcha.repositories;

import com.project.alarcha.entities.AdditionalServiceOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AdditionalServiceOrderRepository extends JpaRepository<AdditionalServiceOrder, Long> {
}
