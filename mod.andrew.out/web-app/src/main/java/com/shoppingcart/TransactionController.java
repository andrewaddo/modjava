package com.shoppingcart;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam; // Import RequestParam

import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Controller
public class TransactionController { // Renamed class

    private static final Logger logger = LoggerFactory.getLogger(TransactionController.class); // Renamed logger

    @Autowired
    private TransactionService transactionService; // Injected TransactionService

    @Autowired
    private OrderService orderService; // Inject OrderService

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
            logger.warn("TransactionController: User not found for payment processing: {}", username);
            return "redirect:/login"; // Redirect to login if user not found
        }
        try {
            Transaction newTransaction = transactionService.createTransaction(user, cartService.getAllCartItems(user)); // Call createTransaction
            logger.info("TransactionController: Transaction created with ID: {}", newTransaction.getTransid());
            cartService.clearCart(user); // Clear the cart after creating the order
            logger.info("TransactionController: Cart cleared for user: {}", username);
            return "redirect:/orders"; // Redirect to orders page after successful payment
        } catch (Exception e) {
            logger.error("TransactionController: Error processing payment for user {}: {}", username, e.getMessage(), e);
            // Optionally, add error to model and return to a payment error page
            return "redirect:/payment-error"; // Redirect to an error page
        }
    }

    @GetMapping("/orders")
    public String showTransactions(Model model, @AuthenticationPrincipal(expression = "username") String username) { // Renamed method
        User user = userRepository.findById(username).orElse(null);
        List<Transaction> transactions = transactionService.getTransactionsByUser(user); // Get transactions
        logger.info("TransactionController: Number of transactions for user {}: {}", username, transactions.size()); // Log transactions
        model.addAttribute("transactions", transactions); // Changed attribute name
        return "orders"; // Still returns orders.html
    }

    @GetMapping("/orders/{transactionId}") // Changed path variable
    public String showTransactionDetails(@PathVariable("transactionId") String transactionId, Model model) { // Renamed method
        model.addAttribute("transaction", transactionService.getTransactionById(transactionId)); // Changed attribute name and method call
        model.addAttribute("orderItems", orderService.getOrdersByTransactionId(transactionId)); // Get order items
        return "order-details"; // Still returns order-details.html
    }

    @GetMapping("/admin/orders")
    public String showAllTransactions(Model model) { // Renamed method
        model.addAttribute("orders", orderService.getAllOrders()); // Changed attribute name and method call
        return "admin/orders";
    }

    @PostMapping("/admin/orders/update/{orderid}/{prodid}") // Changed path variable
    public String updateOrderItemStatus(@PathVariable("orderid") String orderid, @PathVariable("prodid") String prodid, @RequestParam("status") int status) { // Renamed method and parameters
        orderService.updateOrderStatus(new OrderPK(orderid, prodid), status); // Call orderService
        return "redirect:/admin/orders";
    }
}