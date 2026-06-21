package com.springboot.jobportal.service;

import com.springboot.jobportal.entity.User;
import com.springboot.jobportal.repository.UserRepository;
import com.springboot.jobportal.util.CustomUserDetail;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class CustomUserDetailService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository
                .findByEmail(username)
                .orElseThrow(() ->
                    new UsernameNotFoundException("USER NOT FOUND")
                );
        return new CustomUserDetail(user);
    }
}
