package com.project.alarcha.repositories;

import com.project.alarcha.entities.Counter;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface CounterRepository extends JpaRepository<Counter, Long> {
}
