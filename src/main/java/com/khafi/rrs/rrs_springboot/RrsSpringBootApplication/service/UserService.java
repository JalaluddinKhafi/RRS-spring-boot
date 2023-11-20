package com.khafi.rrs.rrs_springboot.RrsSpringBootApplication.service;

import com.khafi.rrs.rrs_springboot.RrsSpringBootApplication.model.User;
import jakarta.transaction.Transactional;

import java.util.List;

public interface UserService {
    void saveUser(User user);
    User getUserById(Long id);
    List<User> getAllUsers();
    @Transactional
    void deleteUser(Long id);
    User findByUsername(String username);
    User findByEmail(String email);
    public int getTotalUserCount();
    public boolean emailExists(String email);
    public boolean usernameExists(String username);

}
