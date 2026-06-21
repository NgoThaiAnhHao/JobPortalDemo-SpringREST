package com.springboot.jobportal.repository;

import com.springboot.jobportal.entity.User;
import com.springboot.jobportal.entity.VerificationToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Optional;

@Repository
public interface VerificationTokenRepository extends JpaRepository<VerificationToken, Long> {
    Optional<VerificationToken> findByToken(String token);

    void deleteByExpiredAtBefore(LocalDateTime time);

    void deleteByUser(User user);
}
