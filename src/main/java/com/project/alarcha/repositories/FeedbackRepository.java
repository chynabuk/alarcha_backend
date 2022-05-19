package com.project.alarcha.repositories;


import com.project.alarcha.entities.Feedback;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FeedbackRepository extends JpaRepository<Feedback, Long> {
    @Query(nativeQuery = true, value = "SELECT * FROM feedback " +
            "WHERE NOT is_replied")
    List<Feedback> getAll();
}
