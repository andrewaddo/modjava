package com.shoppingcart;

import java.util.List;

public interface OrderService {
    Order getOrderById(OrderPK id);
    List<Order> getOrdersByTransactionId(String transactionId);
    Order updateOrderStatus(OrderPK id, int status);
    List<Order> getAllOrders(); // For admin
}