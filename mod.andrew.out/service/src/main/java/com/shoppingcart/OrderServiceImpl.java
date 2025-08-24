package com.shoppingcart;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Override
    public Order getOrderById(OrderPK id) {
        return orderRepository.findById(id).orElse(null);
    }

    @Override
    public List<Order> getOrdersByTransactionId(String transactionId) {
        // Assuming OrderRepository has a method like findByOrderid (which is the transactionId)
        return orderRepository.findByOrderid(transactionId);
    }

    @Override
    @Transactional
    public Order updateOrderStatus(OrderPK id, int status) {
        Order order = getOrderById(id);
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