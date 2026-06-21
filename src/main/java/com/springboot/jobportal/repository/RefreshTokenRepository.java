package com.springboot.jobportal.repository;

import com.springboot.jobportal.entity.RefreshToken;
import com.springboot.jobportal.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.swing.text.html.Option;
import java.time.LocalDateTime;
import java.util.Optional;

@Repository
public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {
    Optional<RefreshToken> findByToken(String token);

    void deleteByUser(User user);

    void deleteByExpiredAtBefore(LocalDateTime time);
}
