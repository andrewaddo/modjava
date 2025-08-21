package com.shashi.webapp.service;

import com.shashi.dataaccess.entity.User;
import com.shashi.dataaccess.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public boolean registerUser(User user) {
        // Check if user with given email already exists
        if (userRepository.existsById(user.getEmail())) {
            return false; // User already exists
        }
        // In a real application, password should be encoded before saving
        userRepository.save(user);
        return true;
    }

    public User loginUser(String email, String password) {
        Optional<User> userOptional = userRepository.findById(email);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            // In a real application, compare encoded passwords
            if (user.getPassword().equals(password)) {
                return user;
            }
        }
        return null; // User not found or invalid credentials
    }

    public User getUserByEmail(String email) {
        return userRepository.findById(email).orElse(null);
    }
}
