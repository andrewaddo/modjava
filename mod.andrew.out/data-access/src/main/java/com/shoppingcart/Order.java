package com.shoppingcart;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Column; // Import Column

@Entity
@Table(name = "orders") // Maps to the 'orders' table in the database
@IdClass(OrderPK.class) // Uses a composite primary key
public class Order {

    @Id
    private String orderid; // Part of the composite primary key

    @Id
    private String prodid; // Part of the composite primary key

    private int quantity;

    private double amount;

    @Column(name = "shipped") // Map to 'shipped' column
    private int status; // 0 for not shipped, 1 for shipped

    

    @ManyToOne
    @JoinColumn(name = "orderid", insertable = false, updatable = false)
    private Transaction transaction; // Many-to-one relationship with Transaction

    @ManyToOne
    @JoinColumn(name = "prodid", insertable = false, updatable = false)
    private Product product; // Many-to-one relationship with Product

    public Order() {
    }

    public Order(String orderid, String prodid, int quantity, double amount, int status) {
        this.orderid = orderid;
        this.prodid = prodid;
        this.quantity = quantity;
        this.amount = amount;
        this.status = status;
    }

    public String getOrderid() {
        return orderid;
    }

    public void setOrderid(String orderid) {
        this.orderid = orderid;
    }

    public String getProdid() {
        return prodid;
    }

    public void setProdid(String prodid) {
        this.prodid = prodid;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public Transaction getTransaction() {
        return transaction;
    }

    public void setTransaction(Transaction transaction) {
        this.transaction = transaction;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }
}