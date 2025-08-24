
package com.shoppingcart;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Controller
public class OrderController {

    private static final Logger logger = LoggerFactory.getLogger(OrderController.class);

    @Autowired
    private OrderService orderService;

    @Autowired
    private CartService cartService;

    @Autowired
    private UserRepository userRepository;

    @GetMapping("/checkout")
    public String checkout(Model model, @AuthenticationPrincipal(expression = "username") String username) {
        User user = userRepository.findById(username).orElse(null);
        if (user == null) {
            return "redirect:/login"; // Redirect to login if user not found
        }
        List<UserCartItem> cartItems = cartService.getAllCartItems(user);
        double totalAmount = cartItems.stream().mapToDouble(item -> item.getProduct().getPrice() * item.getQuantity()).sum();

        model.addAttribute("cartItems", cartItems);
        model.addAttribute("totalAmount", totalAmount);
        return "payment"; // This will resolve to payment.html
    }

    @PostMapping("/orders/pay")
    public String processPayment(@AuthenticationPrincipal(expression = "username") String username) {
        logger.info("Processing payment for user: {}", username);
        User user = userRepository.findById(username).orElse(null);
        if (user == null) {
            return "redirect:/login"; // Redirect to login if user not found
        }
        orderService.createOrder(user, cartService.getAllCartItems(user));
        cartService.clearCart(user); // Clear the cart after creating the order
        return "redirect:/orders"; // Redirect to orders page after successful payment
    }

    @GetMapping("/orders")
    public String showOrders(Model model, @AuthenticationPrincipal(expression = "username") String username) {
        User user = userRepository.findById(username).orElse(null);
        model.addAttribute("orders", orderService.getOrdersByUser(user));
        return "orders";
    }

    @PostMapping("/orders/create")
    public String createOrder(@AuthenticationPrincipal(expression = "username") String username) {
        User user = userRepository.findById(username).orElse(null);
        orderService.createOrder(user, cartService.getAllCartItems(user));
        cartService.clearCart(user); // Clear the cart after creating the order
        return "redirect:/orders";
    }

    @GetMapping("/orders/{orderId}")
    public String showOrderDetails(@PathVariable("orderId") String orderId, Model model) {
        model.addAttribute("order", orderService.getOrderById(orderId));
        return "order-details";
    }

    @GetMapping("/admin/orders")
    public String showAllOrders(Model model) {
        model.addAttribute("orders", orderService.getAllOrders());
        return "admin/orders";
    }

    @PostMapping("/admin/orders/update/{orderId}")
    public String updateOrderStatus(@PathVariable("orderId") String orderId, String status) {
        orderService.updateOrderStatus(orderId, status);
        return "redirect:/admin/orders";
    }
}
