package com.shoppingcart;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List; // Import List

public interface OrderRepository extends JpaRepository<Order, OrderPK> {
    List<Order> findByOrderid(String orderid);
}