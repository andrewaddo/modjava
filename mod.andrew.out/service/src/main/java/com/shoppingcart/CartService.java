
package com.shoppingcart;

import java.util.List; // Added import

public interface CartService {

    // Removed getCart method

    List<UserCartItem> getAllCartItems(User user); // Added method

    void addProductToCart(User user, String productId, int quantity);

    void removeProductFromCart(User user, String productId);

    void updateProductQuantity(User user, String productId, int quantity);

    void clearCart(User user);
}
