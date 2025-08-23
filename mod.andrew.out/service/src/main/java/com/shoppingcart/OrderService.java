
package com.shoppingcart;

import java.util.List;

public interface OrderService {

    Order createOrder(User user, List<UserCartItem> userCartItems);

    List<Order> getOrdersByUser(User user);

    Order getOrderById(String orderId);

    Order updateOrderStatus(String orderId, String status);

    List<Order> getAllOrders();
}
