
package com.shoppingcart;

import java.util.List;

public interface OrderService {

    Order createOrder(User user, List<CartItem> cartItems);

    List<Order> getOrdersForUser(User user);

    Order getOrderById(String orderId);

    void updateOrderStatus(String orderId, String status);

    List<Order> getAllOrders();
}
