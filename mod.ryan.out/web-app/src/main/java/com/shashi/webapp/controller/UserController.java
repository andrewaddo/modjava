package com.shashi.webapp.controller;

import com.shashi.dataaccess.entity.UserEntity;
import com.shashi.service.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/register")
    public String showRegistrationForm(Model model) {
        model.addAttribute("user", new UserEntity());
        return "register";
    }

    @PostMapping("/register")
    public String registerUser(@ModelAttribute("user") UserEntity user, Model model) {
        if (userService.findUserByEmail(user.getEmail()) != null) {
            model.addAttribute("error", "Email already registered.");
            return "register";
        }
        userService.registerUser(user);
        model.addAttribute("message", "Registration successful! Please login.");
        return "login";
    }

    @GetMapping("/login")
    public String showLoginForm(@RequestParam(value = "error", required = false) String error, Model model) {
        if (error != null) {
            model.addAttribute("error", "Invalid email or password.");
        }
        return "login";
    }

    @PostMapping("/login")
    public String loginUser(@RequestParam String email, @RequestParam String password, Model model) {
        if (userService.isValidUser(email, password)) {
            // In a real application, you would set up a session or Spring Security context
            return "redirect:/userHome"; // Redirect to user home page on successful login
        } else {
            return "redirect:/login?error"; // Redirect with error parameter on failed login
        }
    }

    @GetMapping("/userHome")
    public String userHome() {
        return "userHome";
    }

    @GetMapping("/logout")
    public String logout() {
        // In a real application, you would invalidate the session or clear Spring Security context
        return "redirect:/login?logout";
    }
}
