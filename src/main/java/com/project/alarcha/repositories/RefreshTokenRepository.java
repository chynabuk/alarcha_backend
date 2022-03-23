package com.project.alarcha.repositories;

import com.project.alarcha.entities.RefreshToken;
import com.project.alarcha.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {
    Optional<RefreshToken> findById(Long id);

    Optional<RefreshToken> findByToken(String token);

    int deleteByUser(User user);
}
