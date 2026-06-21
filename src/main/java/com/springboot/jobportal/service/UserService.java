package com.springboot.jobportal.service;

import com.springboot.jobportal.entity.User;

import java.util.List;

public interface UserService {
    List<User> findAllUsers();

    User findByEmail(String email);

    void save(User user);

    boolean isAdminExist();

    boolean isEnabled(String email);

    boolean isEmailDuplicate(String email);
}
