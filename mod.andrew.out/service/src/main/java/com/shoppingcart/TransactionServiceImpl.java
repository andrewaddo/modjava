package com.shoppingcart;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.springframework.transaction.annotation.Transactional;
import jakarta.mail.MessagingException; // Import MessagingException

@Service
public class TransactionServiceImpl implements TransactionService { // Renamed class

    @Autowired
    private TransactionRepository transactionRepository; // Injected TransactionRepository

    @Autowired
    private OrderRepository orderRepository; // Injected new OrderRepository

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private EmailService emailService;

    @Override
    @Transactional
    public Transaction createTransaction(User user, List<UserCartItem> userCartItems) { // Renamed method
        Transaction transaction = new Transaction(); // Renamed class
        transaction.setTransid(UUID.randomUUID().toString()); // Set transid
        transaction.setUser(user);
        transaction.setTime(LocalDateTime.now()); // Set time
        transaction.setAmount(userCartItems.stream().mapToDouble(item -> item.getQuantity() * productRepository.findById(item.getProdid()).get().getPrice()).sum()); // Calculate total amount
        // transaction.setStatus("PLACED"); // Removed status from Transaction

        List<Order> orders = new ArrayList<>(); // Renamed list
        for (UserCartItem userCartItem : userCartItems) {
            Product product = productRepository.findById(userCartItem.getProdid()).orElse(null);
            if (product != null) {
                Order order = new Order(); // New Order entity
                order.setOrderid(transaction.getTransid()); // Link to transaction
                order.setProdid(userCartItem.getProdid());
                order.setQuantity(userCartItem.getQuantity());
                order.setAmount(userCartItem.getQuantity() * product.getPrice());
                order.setProduct(product);
                order.setTransaction(transaction); // Set transaction object
                order.setStatus(0); // Set status to 0 (not shipped)
                orders.add(order);
            }
        }

        transaction.setOrders(orders); // Set orders list

        transactionRepository.save(transaction);

        // Send transaction success email
        try {
            emailService.sendTransactionSuccessEmail(user.getEmail(), user.getName(), transaction.getTransid(), transaction.getAmount());
        } catch (MessagingException e) {
            e.printStackTrace(); // Log the exception
        }

        return transaction;
    }

    @Override
    public List<Transaction> getTransactionsByUser(User user) { // Renamed method
        return transactionRepository.findByUser(user);
    }

    @Override
    public Transaction getTransactionById(String transactionId) { // Renamed method
        return transactionRepository.findById(transactionId).orElse(null);
    }

    

    
}
