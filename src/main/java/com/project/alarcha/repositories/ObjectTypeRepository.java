package com.project.alarcha.repositories;

import com.project.alarcha.entities.ObjectType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ObjectTypeRepository extends JpaRepository<ObjectType, Long> {
}