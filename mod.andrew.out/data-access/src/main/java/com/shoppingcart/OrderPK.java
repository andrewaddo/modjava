package com.shoppingcart;

import java.io.Serializable;
import java.util.Objects;

public class OrderPK implements Serializable {

    private String orderid;
    private String prodid;

    public OrderPK() {
    }

    public OrderPK(String orderid, String prodid) {
        this.orderid = orderid;
        this.prodid = prodid;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OrderPK that = (OrderPK) o;
        return Objects.equals(orderid, that.orderid) &&
               Objects.equals(prodid, that.prodid);
    }

    @Override
    public int hashCode() {
        return Objects.hash(orderid, prodid);
    }
}
