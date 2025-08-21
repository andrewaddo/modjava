package com.shashi.webapp.service;

import com.shashi.dataaccess.entity.User;
import com.shashi.dataaccess.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testRegisterUser_Success() {
        User newUser = new User("test@example.com", "Test User", 1234567890L, "123 Test St", 12345, "password");
        when(userRepository.existsById(newUser.getEmail())).thenReturn(false);
        when(userRepository.save(newUser)).thenReturn(newUser);

        boolean result = userService.registerUser(newUser);

        assertTrue(result);
        verify(userRepository, times(1)).existsById(newUser.getEmail());
        verify(userRepository, times(1)).save(newUser);
    }

    @Test
    void testRegisterUser_UserAlreadyExists() {
        User existingUser = new User("test@example.com", "Test User", 1234567890L, "123 Test St", 12345, "password");
        when(userRepository.existsById(existingUser.getEmail())).thenReturn(true);

        boolean result = userService.registerUser(existingUser);

        assertFalse(result);
        verify(userRepository, times(1)).existsById(existingUser.getEmail());
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void testLoginUser_Success() {
        User user = new User("test@example.com", "Test User", 1234567890L, "123 Test St", 12345, "password");
        when(userRepository.findById("test@example.com")).thenReturn(Optional.of(user));

        User loggedInUser = userService.loginUser("test@example.com", "password");

        assertNotNull(loggedInUser);
        assertEquals(user.getEmail(), loggedInUser.getEmail());
        verify(userRepository, times(1)).findById("test@example.com");
    }

    @Test
    void testLoginUser_InvalidPassword() {
        User user = new User("test@example.com", "Test User", 1234567890L, "123 Test St", 12345, "password");
        when(userRepository.findById("test@example.com")).thenReturn(Optional.of(user));

        User loggedInUser = userService.loginUser("test@example.com", "wrongpassword");

        assertNull(loggedInUser);
        verify(userRepository, times(1)).findById("test@example.com");
    }

    @Test
    void testLoginUser_UserNotFound() {
        when(userRepository.findById("nonexistent@example.com")).thenReturn(Optional.empty());

        User loggedInUser = userService.loginUser("nonexistent@example.com", "password");

        assertNull(loggedInUser);
        verify(userRepository, times(1)).findById("nonexistent@example.com");
    }

    @Test
    void testGetUserByEmail_Found() {
        User user = new User("test@example.com", "Test User", 1234567890L, "123 Test St", 12345, "password");
        when(userRepository.findById("test@example.com")).thenReturn(Optional.of(user));

        User foundUser = userService.getUserByEmail("test@example.com");

        assertNotNull(foundUser);
        assertEquals(user.getEmail(), foundUser.getEmail());
        verify(userRepository, times(1)).findById("test@example.com");
    }

    @Test
    void testGetUserByEmail_NotFound() {
        when(userRepository.findById("nonexistent@example.com")).thenReturn(Optional.empty());

        User foundUser = userService.getUserByEmail("nonexistent@example.com");

        assertNull(foundUser);
        verify(userRepository, times(1)).findById("nonexistent@example.com");
    }
}
