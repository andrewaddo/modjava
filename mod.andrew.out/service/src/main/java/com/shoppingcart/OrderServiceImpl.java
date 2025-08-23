
package com.shoppingcart;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private OrderItemRepository orderItemRepository; // Keep this for now, will investigate later

    @Autowired
    private ProductRepository productRepository; // Autowire ProductRepository to fetch product details

    @Override
    public Order createOrder(User user, List<UserCartItem> userCartItems) { // Changed parameter type
        Order order = new Order();
        order.setId(UUID.randomUUID().toString());
        order.setUser(user);
        order.setOrderDate(LocalDateTime.now());
        order.setStatus("PLACED");

        List<OrderItem> orderItems = new ArrayList<>();
        for (UserCartItem userCartItem : userCartItems) { // Iterate over UserCartItem
            OrderItem orderItem = new OrderItem();
            orderItem.setId(UUID.randomUUID().toString());
            orderItem.setOrder(order);
            
            // Fetch product using prodid from UserCartItem
            Product product = productRepository.findById(userCartItem.getProdid()).orElse(null);
            if (product != null) {
                orderItem.setProduct(product);
                orderItem.setQuantity(userCartItem.getQuantity());
                orderItem.setPrice(product.getPrice()); // Use product's price
                orderItems.add(orderItem);
            }
        }

        order.setItems(orderItems);

        orderRepository.save(order);
        orderItemRepository.saveAll(orderItems);

        return order;
    }

    @Override
    public List<Order> getOrdersByUser(User user) {
        return orderRepository.findByUser(user);
    }

    @Override
    public Order getOrderById(String orderId) {
        return orderRepository.findById(orderId).orElse(null);
    }

    @Override
    public Order updateOrderStatus(String orderId, String status) {
        Order order = getOrderById(orderId);
        if (order != null) {
            order.setStatus(status);
            return orderRepository.save(order);
        }
        return null;
    }

    @Override
    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }
}
