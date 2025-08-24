package com.shoppingcart;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "usercart")
@IdClass(UserCartItemPK.class)
public class UserCartItem {

    @Id
    private String username;

    @Id
    private String prodid;

    private int quantity;

    @ManyToOne
    @JoinColumn(name = "prodid", insertable = false, updatable = false)
    private Product product;

    public UserCartItem() {
    }

    public UserCartItem(String username, String prodid, int quantity) {
        this.username = username;
        this.prodid = prodid;
        this.quantity = quantity;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
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

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }
}
