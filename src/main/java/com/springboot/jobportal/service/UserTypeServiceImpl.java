package com.springboot.jobportal.service;

import com.springboot.jobportal.entity.UserType;
import com.springboot.jobportal.enums.UserTypeRoleEnum;
import com.springboot.jobportal.repository.UserTypeRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class UserTypeServiceImpl implements UserTypeService {
    private final UserTypeRepository userTypeRepository;

    @Override
    public UserType findByUserTypeRole(UserTypeRoleEnum userTypeRoleEnum) {
        return userTypeRepository
                .findByUserTypeRole(userTypeRoleEnum)
                .orElseThrow(() ->
                    new RuntimeException("USER TYPE NOT FOUND")
                );
    }
}
