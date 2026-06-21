package com.springboot.jobportal.repository;

import com.springboot.jobportal.entity.UserType;
import com.springboot.jobportal.enums.UserTypeRoleEnum;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserTypeRepository extends JpaRepository<UserType, Long> {
    Optional<UserType> findByUserTypeRole(UserTypeRoleEnum userTypeRoleEnum);
}
