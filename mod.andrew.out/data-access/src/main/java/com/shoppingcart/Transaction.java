package com.shoppingcart;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.CascadeType;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "transactions") // Map to transactions table
public class Transaction {

    @Id
    private String transid; // Primary key is transid

    @ManyToOne
    @JoinColumn(name = "username")
    private User user;

    // This will now map to the new Order entity (order items)
    @OneToMany(mappedBy = "transaction", cascade = CascadeType.ALL)
    private List<Order> orders; // Renamed from 'items' to 'orders'

    private LocalDateTime time; // Renamed from 'orderDate' to 'time'

    private double amount; // Renamed from 'totalAmount' to 'amount'

    public String getTransid() {
        return transid;
    }

    public void setTransid(String transid) {
        this.transid = transid;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public List<Order> getOrders() {
        return orders;
    }

    public void setOrders(List<Order> orders) {
        this.orders = orders;
    }

    public LocalDateTime getTime() {
        return time;
    }

    public void setTime(LocalDateTime time) {
        this.time = time;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }
}