package com.springboot.jobportal.service;

import com.springboot.jobportal.entity.RefreshToken;
import com.springboot.jobportal.entity.User;
import com.springboot.jobportal.repository.RefreshTokenRepository;
import lombok.AllArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@AllArgsConstructor
public class RefreshTokenServiceImpl implements RefreshTokenService {

    private final RefreshTokenRepository refreshTokenRepository;
    
    @Override
    @Transactional
    public RefreshToken createRefreshTokenForUser(User user) {

        // Create new refreshToken
        RefreshToken refreshToken = new RefreshToken();
        refreshToken.setUser(user);
        refreshToken.setToken(UUID.randomUUID().toString());

        // Save and return
        return refreshTokenRepository.save(refreshToken);
    }

    @Override
    public RefreshToken findByToken(String token) {
        return refreshTokenRepository
                .findByToken(token)
                .orElseThrow(() ->
                        new RuntimeException("REFRESH TOKEN NOT FOUND")
                );
    }

    @Override
    @Transactional
    public void deleteByUser(User user) {
        refreshTokenRepository.deleteByUser(user);
        refreshTokenRepository.flush();
    }

    @Override
    @Transactional
    @Scheduled(fixedRate = 300_000) // 5 minutes
    public void deleteExpiredToken() {
        refreshTokenRepository.deleteByExpiredAtBefore(LocalDateTime.now());
    }

    @Override
    public void delete(String refreshToken) {
        RefreshToken refreshTokenFound = findByToken(refreshToken);
        refreshTokenRepository.delete(refreshTokenFound);
    }
}
