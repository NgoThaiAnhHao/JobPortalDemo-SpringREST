package com.springboot.jobportal.controller;

import com.springboot.jobportal.dto.AuthenticationRequest;
import com.springboot.jobportal.dto.AuthenticationResponse;
import com.springboot.jobportal.dto.RegisterRequest;
import com.springboot.jobportal.service.AuthenticationService;
import com.springboot.jobportal.service.VerificationTokenService;
import jakarta.mail.MessagingException;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

/**
 * TODO: Logout xóa token cũ
 */
@RestController
@RequestMapping("/api/auth")
@AllArgsConstructor
public class AuthenticationController {

    private final AuthenticationService authenticationService;
    private final VerificationTokenService verificationTokenService;

    @PostMapping("/register")
    public void register(@Valid @RequestBody RegisterRequest registerRequest) throws MessagingException {
        authenticationService.register(registerRequest);
    }

    @PostMapping("/login")
    public AuthenticationResponse login(@Valid @RequestBody AuthenticationRequest authenticationRequest) {
        return authenticationService.login(authenticationRequest);
    }

    @PostMapping("/logout")
    public void logout(String refreshToken) {
        authenticationService.logout(refreshToken);
    }

    @GetMapping("/refresh-token")
    public AuthenticationResponse refreshToken(@RequestParam String refreshToken) {
        return authenticationService.refreshTokenAndCreateNewAccessToken(refreshToken);
    }

    @PostMapping("/verify-otp")
    public void verifyToken(@RequestParam String token) {
        verificationTokenService.verifyToken(token);
    }

    @PostMapping("/resend-verify-otp")
    public void resendVerifyToken(@RequestParam String email) throws MessagingException {
        verificationTokenService.sendVerifyToken(email);
    }

    @PostMapping("/test-ting")
    public void test(@RequestParam String email) throws MessagingException {
        verificationTokenService.sendVerifyToken(email);
    }

}
