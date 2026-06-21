package com.springboot.jobportal.mapper;

import com.springboot.jobportal.dto.RegisterRequest;
import com.springboot.jobportal.entity.User;
import com.springboot.jobportal.entity.UserType;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;

@AllArgsConstructor
public class UserMapper {

    public static User toEntity(RegisterRequest registerRequest, UserType userType) {
        User user = new User();
        user.setEmail(registerRequest.getEmail());
        user.setEnabled(false);
        user.setUserType(userType);
        user.setProvider(registerRequest.getProvider());
        return user;
    }
}
