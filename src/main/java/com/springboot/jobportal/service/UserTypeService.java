package com.springboot.jobportal.service;

import com.springboot.jobportal.entity.UserType;
import com.springboot.jobportal.enums.UserTypeRoleEnum;

public interface UserTypeService {
    UserType findByUserTypeRole(UserTypeRoleEnum userTypeRoleEnum);
}
