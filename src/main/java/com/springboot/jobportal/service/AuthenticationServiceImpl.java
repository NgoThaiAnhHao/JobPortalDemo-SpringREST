package com.springboot.jobportal.service;

import com.springboot.jobportal.dto.AuthenticationRequest;
import com.springboot.jobportal.dto.AuthenticationResponse;
import com.springboot.jobportal.dto.RegisterRequest;
import com.springboot.jobportal.entity.RefreshToken;
import com.springboot.jobportal.entity.User;
import com.springboot.jobportal.entity.UserType;
import com.springboot.jobportal.enums.UserTypeRoleEnum;
import com.springboot.jobportal.mapper.UserMapper;
import com.springboot.jobportal.util.CustomUserDetail;
import jakarta.mail.MessagingException;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.HashMap;

@Service
@AllArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {

    private final UserService userService;
    private final UserTypeService userTypeService;
    private final VerificationTokenService verificationTokenService;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;
    private final RefreshTokenService refreshTokenService;

    @Override
    @Transactional
    public void register(RegisterRequest registerRequest) throws MessagingException {
        // Checking Password and Confirm Password
        if (!registerRequest.getPassword().equals(registerRequest.getConfirmPassword())) {
            throw new IllegalArgumentException("PASSWORD UNMATCHES WITH CONFIRM PASSWORD");
        }

        // Checking User Type Request is valid? (Type ADMIN: INVALID)
        if ("ADMIN".equals(
                registerRequest.getUserTypeRole().toString()
        ))  {
            throw new IllegalArgumentException("ADMIN IS NOT ALLOWED");
        }

        String emailRequest = registerRequest.getEmail();
        // Checking duplicate email
        if (userService.isEmailDuplicate(emailRequest)) {

            // Checking enable
            if (userService.isEnabled(emailRequest)) {
                // Find User
                User user = userService.findByEmail(registerRequest.getEmail());

                // Clear old otp
                verificationTokenService.deleteOldToken(user);

                // Sender mail to verify
                verificationTokenService.sendVerifyToken(user.getEmail());
                return;
            }

            throw new RuntimeException("DUPLICATE EMAIL");
        }

        // Mapping to entity
        UserType userType = userTypeService.findByUserTypeRole(registerRequest.getUserTypeRole());
        User user = UserMapper.toEntity(registerRequest, userType);
        user.setPassword(
                passwordEncoder.encode(registerRequest.getPassword())
        );

        // Checking is first user, set ADMIN
        if (!userService.isAdminExist()) {
            user.setUserType(
                    userTypeService.findByUserTypeRole(UserTypeRoleEnum.ADMIN)
            );
        }

        // Saving to db
        userService.save(user);

        // Sender mail to verify
        verificationTokenService.sendVerifyToken(user.getEmail());
    }

    @Override
    @Transactional
    public AuthenticationResponse login(AuthenticationRequest authenticationRequest) {
        // Authenticate email and password
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        authenticationRequest.getEmail(), authenticationRequest.getPassword()
                )
        );

        // Get current user to generate token
        CustomUserDetail customUserDetail = (CustomUserDetail) authentication.getPrincipal();

        // If user is null
        if (customUserDetail == null) {
            throw new RuntimeException("LOGIN FAILED");
        }

        // Find user
        User user = userService.findByEmail(customUserDetail.getUsername());

        // Delete old refresh token
        refreshTokenService.deleteByUser(user);

        // Generating refresh token and access token, saving and return
        return new AuthenticationResponse(
                jwtService.generateToken(new HashMap<>(), customUserDetail),
                refreshTokenService.createRefreshTokenForUser(user).getToken()
        );
    }

    @Override
    public void logout(String refreshToken) {
        refreshTokenService.delete(refreshToken);
    }

    @Override
    @Transactional
    public AuthenticationResponse refreshTokenAndCreateNewAccessToken(String refreshToken) {
        // Find refresh token
        RefreshToken refreshTokenFound = refreshTokenService.findByToken(refreshToken);

        // Check expired date
        if (refreshTokenFound.getExpiredAt().isBefore(LocalDateTime.now())) {
            throw new RuntimeException("REFRESH TOKEN EXPIRED");
        }

        // Find user
        User user = refreshTokenFound.getUser();
        CustomUserDetail customUserDetail = new CustomUserDetail(user);

        // Delete old refresh token
        refreshTokenService.deleteByUser(user);

        // Generating refresh token and access token, saving and return
        return new AuthenticationResponse(
                jwtService.generateToken(new HashMap<>(), customUserDetail),
                refreshTokenService.createRefreshTokenForUser(user).getToken()
        );
    }


}
