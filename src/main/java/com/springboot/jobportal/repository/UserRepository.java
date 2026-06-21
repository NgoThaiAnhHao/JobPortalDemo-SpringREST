package com.springboot.jobportal.repository;

import com.springboot.jobportal.entity.User;
import com.springboot.jobportal.entity.UserType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);

    boolean existsByUserTypeAndEnabled(UserType userType, boolean enabled);
}
