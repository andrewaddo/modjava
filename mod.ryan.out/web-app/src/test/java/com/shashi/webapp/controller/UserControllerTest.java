package com.shashi.webapp.controller;

import com.shashi.dataaccess.entity.UserEntity;
import com.shashi.service.user.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.ui.Model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class UserControllerTest {

    @Mock
    private UserService userService;

    @Mock
    private Model model;

    @InjectMocks
    private UserController userController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testShowRegistrationForm() {
        String viewName = userController.showRegistrationForm(model);
        assertEquals("register", viewName);
        verify(model, times(1)).addAttribute(eq("user"), any(UserEntity.class));
    }

    @Test
    void testRegisterUser_success() {
        UserEntity user = new UserEntity();
        user.setEmail("test@example.com");
        user.setPassword("password");

        when(userService.findUserByEmail(user.getEmail())).thenReturn(null);
        when(userService.registerUser(user)).thenReturn(user);

        String viewName = userController.registerUser(user, model);
        assertEquals("login", viewName);
        verify(userService, times(1)).findUserByEmail(user.getEmail());
        verify(userService, times(1)).registerUser(user);
        verify(model, times(1)).addAttribute(eq("message"), anyString());
    }

    @Test
    void testRegisterUser_emailAlreadyRegistered() {
        UserEntity user = new UserEntity();
        user.setEmail("test@example.com");

        when(userService.findUserByEmail(user.getEmail())).thenReturn(user);

        String viewName = userController.registerUser(user, model);
        assertEquals("register", viewName);
        verify(userService, times(1)).findUserByEmail(user.getEmail());
        verify(userService, never()).registerUser(user);
        verify(model, times(1)).addAttribute(eq("error"), anyString());
    }

    @Test
    void testShowLoginForm_noError() {
        String viewName = userController.showLoginForm(null, model);
        assertEquals("login", viewName);
        verify(model, never()).addAttribute(eq("error"), anyString());
    }

    @Test
    void testShowLoginForm_withError() {
        String viewName = userController.showLoginForm("true", model);
        assertEquals("login", viewName);
        verify(model, times(1)).addAttribute(eq("error"), anyString());
    }

    @Test
    void testLoginUser_success() {
        when(userService.isValidUser("test@example.com", "password")).thenReturn(true);

        String viewName = userController.loginUser("test@example.com", "password", model);
        assertEquals("redirect:/userHome", viewName);
        verify(userService, times(1)).isValidUser("test@example.com", "password");
    }

    @Test
    void testLoginUser_failure() {
        when(userService.isValidUser("test@example.com", "wrongpassword")).thenReturn(false);

        String viewName = userController.loginUser("test@example.com", "wrongpassword", model);
        assertEquals("redirect:/login?error", viewName);
        verify(userService, times(1)).isValidUser("test@example.com", "wrongpassword");
    }

    @Test
    void testUserHome() {
        String viewName = userController.userHome();
        assertEquals("userHome", viewName);
    }

    @Test
    void testLogout() {
        String viewName = userController.logout();
        assertEquals("redirect:/login?logout", viewName);
    }
}
