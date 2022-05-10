package com.project.alarcha.repositories;

import com.project.alarcha.entities.NatureType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NatureTypeRepository extends JpaRepository<NatureType, Long> {
}
