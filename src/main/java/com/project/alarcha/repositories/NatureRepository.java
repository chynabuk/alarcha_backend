package com.project.alarcha.repositories;


import com.project.alarcha.entities.Nature;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NatureRepository extends JpaRepository<Nature, Long> {
}
