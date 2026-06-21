package com.springboot.jobportal.service;

import com.springboot.jobportal.entity.User;
import com.springboot.jobportal.entity.UserType;
import com.springboot.jobportal.enums.UserTypeRoleEnum;
import com.springboot.jobportal.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserTypeService userTypeService;

    @Override
    public List<User> findAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public User findByEmail(String email) {
        return userRepository
                .findByEmail(email)
                .orElseThrow(() ->
                        new UsernameNotFoundException("USER NOT FOUND")
                );
    }

    @Override
    @Transactional
    public void save(User user) {
        userRepository.save(user);
    }

    @Override
    public boolean isAdminExist() {
        UserType userTypeAdmin = userTypeService.findByUserTypeRole(UserTypeRoleEnum.ADMIN);
        return userRepository
                .existsByUserTypeAndEnabled(userTypeAdmin, true);
    }

    @Override
    public boolean isEnabled(String email) {
        return !findByEmail(email).isEnabled();
    }

    @Override
    public boolean isEmailDuplicate(String email) {
        return userRepository.findByEmail(email).isPresent();
    }
}
