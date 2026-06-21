package com.springboot.jobportal.service;

import com.springboot.jobportal.entity.User;
import com.springboot.jobportal.entity.VerificationToken;
import com.springboot.jobportal.repository.UserRepository;
import com.springboot.jobportal.repository.VerificationTokenRepository;
import jakarta.mail.MessagingException;
import lombok.AllArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Random;

@Service
@AllArgsConstructor
public class VerificationTokenServiceImpl implements VerificationTokenService {

    private final UserService userService;
    private final VerificationTokenRepository verificationTokenRepository;
    private final MailService mailService;

    @Override
    @Transactional
    public void verifyToken(String token) {
        // Get token to compare
        VerificationToken verificationToken = verificationTokenRepository
                .findByToken(token)
                .orElseThrow(() ->
                        new RuntimeException("OTP INVALID")
                );

        // Get user and set enable and save user to db
        User user = verificationToken.getUser();
        user.setEnabled(true);
        userService.save(user);

        // If save user success, delete this token
        deleteTokenById(verificationToken.getId());
    }

    @Override
    @Transactional
    public void sendVerifyToken(String email) throws MessagingException {
        // Find User
        User user = userService.findByEmail(email);

        // Clear old otp
        deleteOldToken(user);

        // Get random otp with 6 characters
        String verifyToken = getRandomOtp();
        saveVerificationTokenToDatabase(verifyToken, user);

        // Sender mail to verify
        mailService.sendVerificationToken(user.getEmail(), verifyToken);
    }

    @Override
    @Transactional
    public void saveVerificationTokenToDatabase(String verifyToken, User user) {
        // Save verification token to database
        VerificationToken verificationToken = new VerificationToken();
        verificationToken.setToken(verifyToken);
        verificationToken.setUser(user);

        verificationTokenRepository.save(verificationToken);
    }

    @Override
    @Transactional
    public void deleteTokenById(Long id) {
        verificationTokenRepository
                .deleteById(id);
    }

    @Override
    public void deleteOldToken(User user) {
        verificationTokenRepository.deleteByUser(user);
        verificationTokenRepository.flush();
    }

    @Override
    @Transactional
    @Scheduled(fixedRate = 300_000) // 5 minutes
    public void deleteExpiredToken() {
        verificationTokenRepository
                .deleteByExpiredAtBefore(LocalDateTime.now());
    }

    private String getRandomOtp() {
        Random random = new Random();
        return String.format("%06d", random.nextInt(1000000));
    }
}
