
package com.shoppingcart;

public interface CartService {

    Cart getCartForUser(User user);

    void addProductToCart(User user, String productId, int quantity);

    void removeProductFromCart(User user, String productId);

    void updateProductQuantityInCart(User user, String productId, int quantity);
}
