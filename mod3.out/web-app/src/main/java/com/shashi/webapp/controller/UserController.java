package com.shashi.webapp.controller;

import com.shashi.dataaccess.entity.User;
import com.shashi.webapp.service.UserService;
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
        model.addAttribute("user", new User());
        return "register"; // Thymeleaf template name
    }

    @PostMapping("/register")
    public String registerUser(@ModelAttribute("user") User user, Model model) {
        if (userService.registerUser(user)) {
            return "redirect:/login"; // Redirect to login page on successful registration
        } else {
            model.addAttribute("error", "User with this email already exists!");
            return "register"; // Stay on registration page with error
        }
    }

    @GetMapping("/login")
    public String showLoginForm(@RequestParam(value = "error", required = false) String error, Model model) {
        if (error != null) {
            model.addAttribute("error", "Invalid username or password!");
        }
        return "login"; // Thymeleaf template name
    }

    @PostMapping("/login")
    public String loginUser(@RequestParam String email, @RequestParam String password, Model model) {
        User user = userService.loginUser(email, password);
        if (user != null) {
            // In a real application, you would set up a Spring Security session here
            return "redirect:/userHome"; // Redirect to user home on successful login
        } else {
            model.addAttribute("error", "Invalid username or password!");
            return "login"; // Stay on login page with error
        }
    }

    @GetMapping("/userHome")
    public String userHome() {
        return "userHome"; // Thymeleaf template name
    }

    @GetMapping("/logout")
    public String logout() {
        // In a real application, Spring Security handles logout
        return "redirect:/login?logout"; // Redirect to login page with logout success message
    }
}
