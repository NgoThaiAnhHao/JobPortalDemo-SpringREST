package com.springboot.jobportal.dto;

import com.springboot.jobportal.enums.ProviderEnum;
import com.springboot.jobportal.enums.UserTypeRoleEnum;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RegisterRequest {
    @NotBlank(message = "Email is mandatory")
    @Email(message = "Invalid email format")
    private String email;

    @NotBlank(message = "Password is mandatory")
    @Size(min = 5, max = 30, message = "Password must be at least 5 characters long")
    private String password;

    @NotBlank(message = "Password is mandatory")
    @Size(min = 5, max = 30, message = "Confirm password must be at least 5 characters long")
    private String confirmPassword;

    private UserTypeRoleEnum userTypeRole;

    @NotNull(message = "Provider is mandatory")
    private ProviderEnum provider;
}
