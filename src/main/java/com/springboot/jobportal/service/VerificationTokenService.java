package com.springboot.jobportal.service;

import com.springboot.jobportal.entity.User;
import jakarta.mail.MessagingException;

public interface VerificationTokenService {
    void verifyToken(String token);

    void sendVerifyToken(String email) throws MessagingException;

    void saveVerificationTokenToDatabase(String verifyToken, User user);

    void deleteTokenById(Long id);

    void deleteOldToken(User user);

    void deleteExpiredToken();

}
