
package com.shoppingcart;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List; // Added import
import org.springframework.security.core.Authentication; // Added import
import org.springframework.security.core.context.SecurityContextHolder; // Added import
import org.slf4j.Logger; // Added import
import org.slf4j.LoggerFactory; // Added import

@Controller
public class CartController {

    private static final Logger logger = LoggerFactory.getLogger(CartController.class); // Added logger

    @Autowired
    private CartService cartService;

    @Autowired
    private UserRepository userRepository;

    @GetMapping("/cart")
    public String showCart(Model model, @AuthenticationPrincipal String username) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            logger.info("CartController: User not authenticated. Redirecting to login."); // Debugging
            return "redirect:/login";
        }
        username = authentication.getName(); // Get username from Authentication object
        logger.info("CartController: Authenticated username from SecurityContextHolder: {}", username); // Debugging
        User user = userRepository.findById(username).orElse(null);
        if (user == null) { // Added null check for user
            logger.info("CartController: User not found in DB for username: {}. Redirecting to login.", username); // Debugging
            return "redirect:/login"; // Redirect to login if user not found in DB
        }
        List<UserCartItem> cartItems = cartService.getAllCartItems(user); // Changed to getAllCartItems
        logger.info("CartController: Number of items in cart for user {}: {}", username, cartItems.size()); // New log
        model.addAttribute("cartItems", cartItems); // Changed attribute name
        return "cart";
    }

    @PostMapping("/cart/add/{productId}")
    public String addToCart(@PathVariable("productId") String productId, @RequestParam("quantity") int quantity, @AuthenticationPrincipal String username) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            logger.info("CartController: User not authenticated. Redirecting to login."); // Debugging
            return "redirect:/login"; // Redirect to login page if user is not authenticated
        }
        username = authentication.getName(); // Get username from Authentication object
        logger.info("CartController: Authenticated username from SecurityContextHolder: {}", username); // Debugging
        User user = userRepository.findById(username).orElse(null);
        if (user == null) {
            logger.info("CartController: User not found in DB for username: {}. Redirecting to login.", username); // Debugging
            return "redirect:/login"; // Redirect to login if user not found in DB
        }
        logger.info("CartController: User found: {}, Role: {}", user.getEmail(), user.getRole()); // Debugging
        cartService.addProductToCart(user, productId, quantity);
        return "redirect:/cart";
    }

        @GetMapping("/cart/remove/{productId}")
    public String removeFromCart(@PathVariable("productId") String productId, @AuthenticationPrincipal String username) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            logger.info("CartController: User not authenticated. Redirecting to login."); // Debugging
            return "redirect:/login";
        }
        username = authentication.getName(); // Get username from Authentication object
        logger.info("CartController: Authenticated username from SecurityContextHolder: {}", username); // Debugging
        User user = userRepository.findById(username).orElse(null);
        if (user == null) { // Added null check for user
            logger.info("CartController: User not found in DB for username: {}. Redirecting to login.", username); // Debugging
            return "redirect:/login"; // Redirect to login if user not found in DB
        }
        logger.info("CartController: User found: {}, Role: {}", user.getEmail(), user.getRole()); // Debugging
        cartService.removeProductFromCart(user, productId);
        return "redirect:/cart";
    }

            @PostMapping("/cart/update/{productId}")
    public String updateCart(@PathVariable("productId") String productId, @RequestParam("quantity") int quantity, @AuthenticationPrincipal String username) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            logger.info("CartController: User not authenticated. Redirecting to login."); // Debugging
            return "redirect:/login";
        }
        username = authentication.getName(); // Get username from Authentication object
        logger.info("CartController: Authenticated username from SecurityContextHolder: {}", username); // Debugging
        User user = userRepository.findById(username).orElse(null);
        if (user == null) { // Added null check for user
            logger.info("CartController: User not found in DB for username: {}. Redirecting to login.", username); // Debugging
            return "redirect:/login"; // Redirect to login if user not found in DB
        }
        logger.info("CartController: User found: {}, Role: {}", user.getEmail(), user.getRole()); // Debugging
        cartService.updateProductQuantity(user, productId, quantity);
        return "redirect:/cart";
    }
}
