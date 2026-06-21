package com.springboot.jobportal.service;

import com.springboot.jobportal.util.CustomUserDetail;

import java.util.Map;

public interface JwtService {
    String extractUsername(String token);

    boolean isTokenValid(String token, CustomUserDetail userDetails);

    String generateToken(Map<String, Object> claims, CustomUserDetail userDetails);
}
