package com.shashi.service.cart;

import com.shashi.dataaccess.entity.UserCartEntity;

import java.util.List;

public interface CartService {
    UserCartEntity addProductToCart(String username, String prodid, Integer quantity);
    UserCartEntity updateProductQuantityInCart(String username, String prodid, Integer quantity);
    void removeProductFromCart(String username, String prodid);
    List<UserCartEntity> getCartItemsByUsername(String username);
    void clearCart(String username);
}
