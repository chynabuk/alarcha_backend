package com.project.alarcha.service;

import com.project.alarcha.entities.RefreshToken;
import com.project.alarcha.models.RefreshTokenRequest;
import org.springframework.http.ResponseEntity;

import java.util.Optional;

public interface RefreshTokenService {
    public ResponseEntity<?> refreshtoken(RefreshTokenRequest request);
    public Optional<RefreshToken> findByToken(String token);
    public RefreshToken createRefreshToken(Long userId);
    public RefreshToken verifyExpiration(RefreshToken token);
    public int deleteByUserId(Long userId);
}
