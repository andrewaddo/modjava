package com.shashi.webapp.service;

import com.shashi.dataaccess.entity.Orders;
import com.shashi.dataaccess.entity.OrderId;
import com.shashi.dataaccess.entity.Product;
import com.shashi.dataaccess.entity.Transactions;
import com.shashi.dataaccess.entity.Usercart;
import com.shashi.dataaccess.entity.User;
import com.shashi.dataaccess.repository.OrdersRepository;
import com.shashi.dataaccess.repository.ProductRepository;
import com.shashi.dataaccess.repository.TransactionsRepository;
import com.shashi.dataaccess.repository.UsercartRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class OrderService {

    @Autowired
    private OrdersRepository ordersRepository;

    @Autowired
    private TransactionsRepository transactionsRepository;

    @Autowired
    private UsercartRepository usercartRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private EmailService emailService;

    @Autowired
    private UserService userService;

    // Setters for testing purposes
    public void setOrdersRepository(OrdersRepository ordersRepository) {
        this.ordersRepository = ordersRepository;
    }

    public void setTransactionsRepository(TransactionsRepository transactionsRepository) {
        this.transactionsRepository = transactionsRepository;
    }

    public void setUsercartRepository(UsercartRepository usercartRepository) {
        this.usercartRepository = usercartRepository;
    }

    public void setProductRepository(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public void setEmailService(EmailService emailService) {
        this.emailService = emailService;
    }

    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    @Transactional
    public String placeOrder(String username, BigDecimal totalAmount) {
        List<Usercart> cartItems = usercartRepository.findById_Username(username);
        if (cartItems.isEmpty()) {
            return null; // Cart is empty, cannot place order
        }

        String orderId = "TR" + UUID.randomUUID().toString().substring(0, 10).toUpperCase();

        // Create orders for each item in the cart
        for (Usercart item : cartItems) {
            Orders order = new Orders(
                    new OrderId(orderId, item.getId().getProdid()),
                    item.getQuantity(),
                    productRepository.findById(item.getId().getProdid())
                            .map(product -> product.getPprice().multiply(BigDecimal.valueOf(item.getQuantity())))
                            .orElse(BigDecimal.ZERO),
                    0 // Not shipped yet
            );
            ordersRepository.save(order);

            // Update product quantity (inventory)
            productRepository.findById(item.getId().getProdid()).ifPresent(product -> {
                product.setPquantity(product.getPquantity() - item.getQuantity());
                productRepository.save(product);
            });
        }

        // Create a transaction record
        Transactions transaction = new Transactions(
                orderId, // Using orderId as transid for simplicity, can be separate UUID
                username,
                LocalDateTime.now(),
                totalAmount
        );
        transactionsRepository.save(transaction);

        // Clear the user's cart
        usercartRepository.deleteAll(cartItems);

        // Send order confirmation email
        User user = userService.getUserByEmail(username);
        if (user != null) {
            emailService.sendOrderConfirmation(user.getEmail(), orderId, user.getName());
        }

        return orderId;
    }

    public List<Orders> getOrdersByOrderId(String orderId) {
        return ordersRepository.findById_Orderid(orderId);
    }

    public List<Transactions> getTransactionsByUsername(String username) {
        return transactionsRepository.findByUsername(username);
    }

    public List<Orders> getAllOrders() {
        return ordersRepository.findAll();
    }

    @Transactional
    public boolean updateOrderStatus(String orderId, String prodid, int shippedStatus) {
        OrderId id = new OrderId(orderId, prodid);
        Optional<Orders> orderOptional = ordersRepository.findById(id);
        if (orderOptional.isPresent()) {
            Orders order = orderOptional.get();
            order.setShipped(shippedStatus);
            ordersRepository.save(order);

            // Send shipping update email if status is shipped
            if (shippedStatus == 1) {
                // Need to get username from transaction or order
                transactionsRepository.findById(orderId).ifPresent(transaction -> {
                    User user = userService.getUserByEmail(transaction.getUsername());
                    if (user != null) {
                        emailService.sendShippingUpdate(user.getEmail(), orderId, prodid, user.getName());
                    }
                });
            }
            return true;
        }
        return false;
    }

    public List<Orders> getOrdersByShippedStatus(int shippedStatus) {
        return ordersRepository.findAll().stream()
                .filter(order -> order.getShipped() == shippedStatus)
                .collect(Collectors.toList());
    }

    public List<Orders> getOrdersForUser(String username) {
        List<Transactions> userTransactions = transactionsRepository.findByUsername(username);
        List<String> orderIds = userTransactions.stream()
                .map(Transactions::getTransid)
                .collect(Collectors.toList());
        return ordersRepository.findById_OrderidIn(orderIds);
    }
}
