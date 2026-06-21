package com.springboot.jobportal.service;

import com.springboot.jobportal.dto.AuthenticationRequest;
import com.springboot.jobportal.dto.AuthenticationResponse;
import com.springboot.jobportal.dto.RegisterRequest;
import jakarta.mail.MessagingException;

public interface AuthenticationService {
    void register(RegisterRequest registerRequest) throws MessagingException;

    AuthenticationResponse login(AuthenticationRequest authenticationRequest);

    void logout(String refreshToken);

    AuthenticationResponse refreshTokenAndCreateNewAccessToken(String refreshToken);
}
