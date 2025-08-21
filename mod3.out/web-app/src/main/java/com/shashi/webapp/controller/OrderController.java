package com.shashi.webapp.controller;

import com.shashi.dataaccess.entity.Orders;
import com.shashi.dataaccess.entity.Product;
import com.shashi.dataaccess.entity.Transactions;
import com.shashi.dataaccess.entity.Usercart;
import com.shashi.webapp.service.CartService;
import com.shashi.webapp.service.OrderService;
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
public class OrderController {

    @Autowired
    private CartService cartService;

    @Autowired
    private OrderService orderService;

    @Autowired
    private ProductService productService;

    private String getCurrentUsername() {
        return SecurityContextHolder.getContext().getAuthentication().getName();
    }

    @GetMapping("/checkout")
    public String showCheckout(Model model) {
        String username = getCurrentUsername();
        List<Usercart> cartItems = cartService.getCartItemsByUsername(username);
        Map<String, Product> productsInCart = cartItems.stream()
                .map(item -> productService.getProductById(item.getId().getProdid()))
                .filter(java.util.Optional::isPresent)
                .map(java.util.Optional::get)
                .collect(Collectors.toMap(Product::getPid, product -> product));

        model.addAttribute("cartItems", cartItems);
        model.addAttribute("productsInCart", productsInCart);
        model.addAttribute("cartTotal", cartService.getCartTotal(username));
        return "checkout"; // Thymeleaf template
    }

    @PostMapping("/placeOrder")
    public String placeOrder(@RequestParam("totalAmount") BigDecimal totalAmount) {
        String username = getCurrentUsername();
        String orderId = orderService.placeOrder(username, totalAmount);
        if (orderId != null) {
            return "redirect:/orderConfirmation?orderId=" + orderId;
        } else {
            return "redirect:/cartDetails?error=emptyCart";
        }
    }

    @GetMapping("/orderConfirmation")
    public String orderConfirmation(@RequestParam("orderId") String orderId, Model model) {
        List<Orders> orders = orderService.getOrdersByOrderId(orderId);
        model.addAttribute("orderId", orderId);
        model.addAttribute("orders", orders);
        return "order-confirmation"; // Thymeleaf template
    }

    @GetMapping("/orderHistory")
    public String orderHistory(Model model) {
        String username = getCurrentUsername();
        List<Transactions> transactions = orderService.getTransactionsByUsername(username);
        model.addAttribute("transactions", transactions);
        return "order-history"; // Thymeleaf template
    }

    @GetMapping("/trackOrder")
    public String trackOrder(@RequestParam("orderId") String orderId, Model model) {
        List<Orders> orders = orderService.getOrdersByOrderId(orderId);
        model.addAttribute("orderId", orderId);
        model.addAttribute("orders", orders);
        return "track-order"; // Thymeleaf template
    }
}
