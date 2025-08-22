package com.shashi.service.user;

import com.shashi.dataaccess.entity.UserEntity;
import com.shashi.dataaccess.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserEntity registerUser(UserEntity user) {
        // In a real application, you would hash the password here before saving
        return userRepository.save(user);
    }

    @Override
    public UserEntity findUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    public boolean isValidUser(String email, String password) {
        UserEntity user = userRepository.findByEmail(email);
        // In a real application, you would compare hashed passwords
        return user != null && user.getPassword().equals(password);
    }
}
