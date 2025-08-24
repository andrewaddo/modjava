package com.shoppingcart;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ProfileController {

    @Autowired
    private UserRepository userRepository;

    @GetMapping("/profile")
    public String showProfile(Model model, @AuthenticationPrincipal(expression = "username") String username) {
        User user = userRepository.findById(username).orElse(null);
        if (user == null) {
            return "redirect:/login"; // Redirect to login if user not found
        }
        model.addAttribute("user", user);
        return "profile";
    }
}
