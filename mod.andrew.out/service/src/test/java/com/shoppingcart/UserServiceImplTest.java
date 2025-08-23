
package com.shoppingcart;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserServiceImpl userService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testRegisterUser() {
        User user = new User();
        user.setEmail("test@test.com");
        user.setPassword("password");

        when(passwordEncoder.encode("password")).thenReturn("encodedPassword");
        when(userRepository.save(user)).thenReturn(user);

        User registeredUser = userService.registerUser(user);

        assertEquals("encodedPassword", registeredUser.getPassword());
        verify(userRepository, times(1)).save(user);
    }

    @Test
    void testLoginUser_Success() {
        User user = new User();
        user.setEmail("test@test.com");
        user.setPassword("encodedPassword");

        when(userRepository.findById("test@test.com")).thenReturn(Optional.of(user));
        when(passwordEncoder.matches("password", "encodedPassword")).thenReturn(true);

        User loggedInUser = userService.loginUser("test@test.com", "password");

        assertNotNull(loggedInUser);
        assertEquals("test@test.com", loggedInUser.getEmail());
    }

    @Test
    void testLoginUser_UserNotFound() {
        when(userRepository.findById("test@test.com")).thenReturn(Optional.empty());

        User loggedInUser = userService.loginUser("test@test.com", "password");

        assertNull(loggedInUser);
    }

    @Test
    void testLoginUser_InvalidPassword() {
        User user = new User();
        user.setEmail("test@test.com");
        user.setPassword("encodedPassword");

        when(userRepository.findById("test@test.com")).thenReturn(Optional.of(user));
        when(passwordEncoder.matches("password", "encodedPassword")).thenReturn(false);

        User loggedInUser = userService.loginUser("test@test.com", "password");

        assertNull(loggedInUser);
    }
}
