package com.shashi.webapp.controller;

import com.shashi.dataaccess.entity.User;
import com.shashi.webapp.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;

import com.shashi.webapp.config.TestSecurityConfig;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UserController.class)
@Import(TestSecurityConfig.class)
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @Test
    void testShowRegistrationForm() throws Exception {
        mockMvc.perform(get("/register"))
                .andExpect(status().isOk())
                .andExpect(view().name("register"))
                .andExpect(model().attributeExists("user"));
    }

    @Test
    void testRegisterUser_Success() throws Exception {
        User newUser = new User("test@example.com", "Test User", 1234567890L, "123 Test St", 12345, "password");
        when(userService.registerUser(any(User.class))).thenReturn(true);

        mockMvc.perform(post("/register")
                        .flashAttr("user", newUser))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/login"));

        verify(userService, times(1)).registerUser(any(User.class));
    }

    @Test
    void testRegisterUser_UserExists() throws Exception {
        User existingUser = new User("test@example.com", "Test User", 1234567890L, "123 Test St", 12345, "password");
        when(userService.registerUser(any(User.class))).thenReturn(false);

        mockMvc.perform(post("/register")
                        .flashAttr("user", existingUser))
                .andExpect(status().isOk())
                .andExpect(view().name("register"))
                .andExpect(model().attributeExists("error"));

        verify(userService, times(1)).registerUser(any(User.class));
    }

    @Test
    void testShowLoginForm() throws Exception {
        mockMvc.perform(get("/login"))
                .andExpect(status().isOk())
                .andExpect(view().name("login"));
    }

    @Test
    void testShowLoginFormWithError() throws Exception {
        mockMvc.perform(get("/login").param("error", "true"))
                .andExpect(status().isOk())
                .andExpect(view().name("login"))
                .andExpect(model().attributeExists("error"));
    }

    @Test
    void testLoginUser_Success() throws Exception {
        User user = new User("test@example.com", "Test User", 1234567890L, "123 Test St", 12345, "password");
        when(userService.loginUser("test@example.com", "password")).thenReturn(user);

        mockMvc.perform(post("/login")
                        .param("email", "test@example.com")
                        .param("password", "password"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/userHome"));

        verify(userService, times(1)).loginUser("test@example.com", "password");
    }

    @Test
    void testLoginUser_Failure() throws Exception {
        when(userService.loginUser("test@example.com", "wrongpassword")).thenReturn(null);

        mockMvc.perform(post("/login")
                        .param("email", "test@example.com")
                        .param("password", "wrongpassword"))
                .andExpect(status().isOk())
                .andExpect(view().name("login"))
                .andExpect(model().attributeExists("error"));

        verify(userService, times(1)).loginUser("test@example.com", "wrongpassword");
    }

    @Test
    void testUserHome() throws Exception {
        mockMvc.perform(get("/userHome"))
                .andExpect(status().isOk())
                .andExpect(view().name("userHome"));
    }

    @Test
    void testLogout() throws Exception {
        mockMvc.perform(get("/logout"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/login?logout"));
    }
}