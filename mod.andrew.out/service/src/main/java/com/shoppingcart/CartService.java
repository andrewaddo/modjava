
package com.shoppingcart;

public interface CartService {

    Cart getCart(User user);

    void addProductToCart(User user, String productId, int quantity);

    void removeProductFromCart(User user, String productId);

    void updateProductQuantity(User user, String productId, int quantity);

    void clearCart(User user);
}
