package com.shashi.webapp.controller;

import com.shashi.dataaccess.entity.ProductEntity;
import com.shashi.dataaccess.entity.UserCartEntity;
import com.shashi.service.cart.CartService;
import com.shashi.service.product.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/cart")
public class CartController {

    @Autowired
    private CartService cartService;

    @Autowired
    private ProductService productService;

    // Placeholder for logged-in user's email. In a real app, this would come from Spring Security context.
    private String getCurrentUserEmail() {
        return "guest@gmail.com"; // For now, hardcode a user for testing
    }

    @PostMapping("/add")
    public String addProductToCart(@RequestParam("prodid") String prodid,
                                   @RequestParam(value = "quantity", defaultValue = "1") Integer quantity) {
        String username = getCurrentUserEmail();
        cartService.addProductToCart(username, prodid, quantity);
        return "redirect:/cart/view";
    }

    @GetMapping("/view")
    public String viewCart(Model model) {
        String username = getCurrentUserEmail();
        List<UserCartEntity> cartItems = cartService.getCartItemsByUsername(username);

        BigDecimal grandTotal = BigDecimal.ZERO;
        for (UserCartEntity item : cartItems) {
            Optional<ProductEntity> productOptional = productService.getProductById(item.getProdid());
            if (productOptional.isPresent()) {
                ProductEntity product = productOptional.get();
                item.setPname(product.getPname()); // Add product name to cart item for display
                item.setPprice(product.getPprice()); // Add product price to cart item for display
                grandTotal = grandTotal.add(product.getPprice().multiply(BigDecimal.valueOf(item.getQuantity())));
            }
        }

        model.addAttribute("cartItems", cartItems);
        model.addAttribute("grandTotal", grandTotal);
        return "cartDetails";
    }

    @PostMapping("/update")
    public String updateCartItemQuantity(@RequestParam("prodid") String prodid,
                                         @RequestParam("quantity") Integer quantity) {
        String username = getCurrentUserEmail();
        if (quantity <= 0) {
            cartService.removeProductFromCart(username, prodid);
        } else {
            cartService.updateProductQuantityInCart(username, prodid, quantity);
        }
        return "redirect:/cart/view";
    }

    @GetMapping("/remove")
    public String removeCartItem(@RequestParam("prodid") String prodid) {
        String username = getCurrentUserEmail();
        cartService.removeProductFromCart(username, prodid);
        return "redirect:/cart/view";
    }

    @GetMapping("/clear")
    public String clearCart() {
        String username = getCurrentUserEmail();
        cartService.clearCart(username);
        return "redirect:/cart/view";
    }
}
