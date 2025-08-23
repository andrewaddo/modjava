
package com.shoppingcart;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import jakarta.mail.MessagingException;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private EmailService emailService;

    @Override
    public User registerUser(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRole("USER");
        User savedUser = userRepository.save(user);
        try {
            emailService.sendRegistrationSuccessEmail(savedUser.getEmail(), savedUser.getName());
        } catch (MessagingException e) {
            e.printStackTrace();
        }
        return savedUser;
    }

    @Override
    public User loginUser(String email, String password) {
        User user = userRepository.findById(email).orElse(null);
        if (user != null && passwordEncoder.matches(password, user.getPassword())) {
            return user;
        }
        return null;
    }
}
