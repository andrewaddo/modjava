package com.shashi.webapp.controller;

import com.shashi.dataaccess.entity.Product;
import com.shashi.dataaccess.entity.Usercart;
import com.shashi.webapp.service.CartService;
import com.shashi.webapp.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
public class CartController {

    @Autowired
    private CartService cartService;

    @Autowired
    private ProductService productService;

    private String getCurrentUsername() {
        return SecurityContextHolder.getContext().getAuthentication().getName();
    }

    @PostMapping("/addToCart")
    public String addToCart(@RequestParam("prodid") String prodid,
                            @RequestParam(value = "quantity", defaultValue = "1") int quantity) {
        String username = getCurrentUsername();
        cartService.addProductToCart(username, prodid, quantity);
        return "redirect:/cartDetails";
    }

    @GetMapping("/cartDetails")
    public String viewCart(Model model) {
        String username = getCurrentUsername();
        List<Usercart> cartItems = cartService.getCartItemsByUsername(username);
        
        // Fetch product details for each item in the cart
        Map<String, Product> productsInCart = cartItems.stream()
                .map(item -> productService.getProductById(item.getId().getProdid()))
                .filter(java.util.Optional::isPresent)
                .map(java.util.Optional::get)
                .collect(Collectors.toMap(Product::getPid, product -> product));

        model.addAttribute("cartItems", cartItems);
        model.addAttribute("productsInCart", productsInCart);
        model.addAttribute("cartTotal", cartService.getCartTotal(username));
        return "cartDetails"; // Thymeleaf template name
    }

    @PostMapping("/updateCart")
    public String updateCart(@RequestParam("prodid") String prodid,
                             @RequestParam("quantity") int quantity) {
        String username = getCurrentUsername();
        cartService.updateProductQuantity(username, prodid, quantity);
        return "redirect:/cartDetails";
    }

    @GetMapping("/removeCart")
    public String removeCart(@RequestParam("prodid") String prodid) {
        String username = getCurrentUsername();
        cartService.removeProductFromCart(username, prodid);
        return "redirect:/cartDetails";
    }
}
