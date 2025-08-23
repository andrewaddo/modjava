
package com.shoppingcart;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class OrderController {

    @Autowired
    private OrderService orderService;

    @Autowired
    private CartService cartService;

    @Autowired
    private UserRepository userRepository;

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
