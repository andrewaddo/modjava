package com.shashi.dataaccess.repository;

import com.shashi.dataaccess.entity.Orders;
import com.shashi.dataaccess.entity.OrderId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrdersRepository extends JpaRepository<Orders, OrderId> {
    List<Orders> findById_Orderid(String orderid);
    List<Orders> findById_OrderidIn(List<String> orderIds);
}
