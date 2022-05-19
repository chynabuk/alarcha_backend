package com.project.alarcha.repositories;

import com.project.alarcha.entities.BaseCheck;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface BaseCheckRepository extends JpaRepository<BaseCheck, Long> {
    @Query(nativeQuery = true, value = "SELECT * FROM base_check " +
            "WHERE id=(SELECT max(id) FROM base_check)")
    BaseCheck getLastRecord();
}
