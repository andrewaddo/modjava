package com.shashi.service.user;

import com.shashi.dataaccess.entity.UserEntity;
import com.shashi.dataaccess.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserServiceImpl userService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testRegisterUser() {
        UserEntity user = new UserEntity();
        user.setEmail("test@example.com");
        user.setPassword("password");

        when(userRepository.save(user)).thenReturn(user);

        UserEntity registeredUser = userService.registerUser(user);

        assertNotNull(registeredUser);
        assertEquals("test@example.com", registeredUser.getEmail());
        verify(userRepository, times(1)).save(user);
    }

    @Test
    void testFindUserByEmail() {
        UserEntity user = new UserEntity();
        user.setEmail("test@example.com");

        when(userRepository.findByEmail("test@example.com")).thenReturn(user);

        UserEntity foundUser = userService.findUserByEmail("test@example.com");

        assertNotNull(foundUser);
        assertEquals("test@example.com", foundUser.getEmail());
        verify(userRepository, times(1)).findByEmail("test@example.com");
    }

    @Test
    void testIsValidUser_validCredentials() {
        UserEntity user = new UserEntity();
        user.setEmail("test@example.com");
        user.setPassword("password");

        when(userRepository.findByEmail("test@example.com")).thenReturn(user);

        assertTrue(userService.isValidUser("test@example.com", "password"));
        verify(userRepository, times(1)).findByEmail("test@example.com");
    }

    @Test
    void testIsValidUser_invalidPassword() {
        UserEntity user = new UserEntity();
        user.setEmail("test@example.com");
        user.setPassword("password");

        when(userRepository.findByEmail("test@example.com")).thenReturn(user);

        assertFalse(userService.isValidUser("test@example.com", "wrongpassword"));
        verify(userRepository, times(1)).findByEmail("test@example.com");
    }

    @Test
    void testIsValidUser_userNotFound() {
        when(userRepository.findByEmail("nonexistent@example.com")).thenReturn(null);

        assertFalse(userService.isValidUser("nonexistent@example.com", "password"));
        verify(userRepository, times(1)).findByEmail("nonexistent@example.com");
    }
}
