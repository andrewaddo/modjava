package com.shoppingcart;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "demands")
@IdClass(DemandPK.class)
public class Demand {

    @Id
    private String username;

    @Id
    private String prodid;

    private int demandQty;

    @ManyToOne
    @JoinColumn(name = "username", insertable = false, updatable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "prodid", insertable = false, updatable = false)
    private Product product;

    public Demand() {
    }

    public Demand(String username, String prodid, int demandQty) {
        this.username = username;
        this.prodid = prodid;
        this.demandQty = demandQty;
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

    public int getDemandQty() {
        return demandQty;
    }

    public void setDemandQty(int demandQty) {
        this.demandQty = demandQty;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }
}
