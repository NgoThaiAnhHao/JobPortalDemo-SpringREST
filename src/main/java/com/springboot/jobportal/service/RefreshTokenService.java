package com.springboot.jobportal.service;

import com.springboot.jobportal.entity.RefreshToken;
import com.springboot.jobportal.entity.User;

public interface RefreshTokenService {
    RefreshToken createRefreshTokenForUser(User user);

    RefreshToken findByToken(String token);

    void deleteByUser(User user);

    void deleteExpiredToken();

    void delete(String refreshToken);
}
