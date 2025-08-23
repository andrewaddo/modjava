
package com.shoppingcart;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class CartController {

    @Autowired
    private CartService cartService;

    @Autowired
    private UserRepository userRepository;

    @GetMapping("/cart")
    public String showCart(Model model, @AuthenticationPrincipal(expression = "username") String username) {
        User user = userRepository.findById(username).orElse(null);
        model.addAttribute("cart", cartService.getCart(user));
        return "cart";
    }

    @PostMapping("/cart/add/{productId}")
    public String addToCart(@PathVariable("productId") String productId, @RequestParam("quantity") int quantity, @AuthenticationPrincipal(expression = "username") String username) {
        User user = userRepository.findById(username).orElse(null);
        cartService.addProductToCart(user, productId, quantity);
        return "redirect:/cart";
    }

    @GetMapping("/cart/remove/{productId}")
    public String removeFromCart(@PathVariable("productId") String productId, @AuthenticationPrincipal(expression = "username") String username) {
        User user = userRepository.findById(username).orElse(null);
        cartService.removeProductFromCart(user, productId);
        return "redirect:/cart";
    }

    @PostMapping("/cart/update/{productId}")
    public String updateCart(@PathVariable("productId") String productId, @RequestParam("quantity") int quantity, @AuthenticationPrincipal(expression = "username") String username) {
        User user = userRepository.findById(username).orElse(null);
        cartService.updateProductQuantity(user, productId, quantity);
        return "redirect:/cart";
    }
}
