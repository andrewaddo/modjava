package com.shoppingcart;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Column;

import org.springframework.format.annotation.NumberFormat;

import jakarta.validation.constraints.NotNull;

@Entity
@Table(name = "product")
public class Product {

    @Id
    @Column(name = "pid")
    private String pid;
    @NotNull
    @Column(name = "name")
    private String name;
    @NotNull
    @Column(name = "info")
    private String info;
    @NotNull
    @NumberFormat(style = NumberFormat.Style.NUMBER)
    @Column(name = "price")
    private double price;
    @NotNull
    @Column(name = "quantity")
    private int quantity;
    @Column(name = "image")
    private String image;
    

    

    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    
}
