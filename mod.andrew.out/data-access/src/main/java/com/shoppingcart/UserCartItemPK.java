package com.shoppingcart;

import java.io.Serializable;
import java.util.Objects;

public class UserCartItemPK implements Serializable {

    private String username;
    private String prodid;

    public UserCartItemPK() {
    }

    public UserCartItemPK(String username, String prodid) {
        this.username = username;
        this.prodid = prodid;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserCartItemPK that = (UserCartItemPK) o;
        return Objects.equals(username, that.username) &&
               Objects.equals(prodid, that.prodid);
    }

    @Override
    public int hashCode() {
        return Objects.hash(username, prodid);
    }
}
