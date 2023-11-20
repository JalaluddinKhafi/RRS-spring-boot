package com.khafi.rrs.rrs_springboot.RrsSpringBootApplication.ServiceImpl;

import com.khafi.rrs.rrs_springboot.RrsSpringBootApplication.model.User;
import com.khafi.rrs.rrs_springboot.RrsSpringBootApplication.repository.UserRepository;
import com.khafi.rrs.rrs_springboot.RrsSpringBootApplication.service.UserService;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }
    @Override
    public void saveUser(User user) {
        userRepository.save(user);
    }
    @Override
    public User getUserById(Long id) {
        return userRepository.findById(id).get();
    }

    @Override
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @Override
    @Transactional
    public void deleteUser(Long id) {
        try {
            userRepository.deleteById(id);
            System.out.println("User deleted by ID: " + id);
        } catch (Exception e) {
            System.out.println("Error deleting User with ID: {} " + id + " " + e);
            // Handle the exception as needed.
        }
    }

    @Override
    public User findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    @Override
    public User findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    public int getTotalUserCount() {
        List<User> userList=getAllUsers();
        int totalUser=userList.size();
        return totalUser;
    }
    @Override
    public boolean emailExists(String email) {
        return userRepository.existsByEmail(email);
    }
    @Override
    public boolean usernameExists(String username){
    return userRepository.existsByUsername(username);
    }
    public void updateUser(User updatedUser) {
        // Perform any necessary business logic or validation

        // Save the updated user to the database
        userRepository.save(updatedUser);
    }
}
