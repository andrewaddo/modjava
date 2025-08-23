
package com.shoppingcart;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class CartServiceImpl implements CartService {

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private CartItemRepository cartItemRepository;

    @Autowired
    private ProductRepository productRepository;

    @Override
    public Cart getCartForUser(User user) {
        Cart cart = cartRepository.findByUser(user);
        if (cart == null) {
            cart = new Cart();
            cart.setUser(user);
            cart.setItems(new ArrayList<>());
            cartRepository.save(cart);
        }
        return cart;
    }

    @Override
    public void addProductToCart(User user, String productId, int quantity) {
        Cart cart = getCartForUser(user);
        Product product = productRepository.findById(productId).orElse(null);

        if (product != null) {
            CartItem cartItem = new CartItem();
            cartItem.setCart(cart);
            cartItem.setProduct(product);
            cartItem.setQuantity(quantity);
            cartItemRepository.save(cartItem);
        }
    }

    @Override
    public void removeProductFromCart(User user, String productId) {
        Cart cart = getCartForUser(user);
        Product product = productRepository.findById(productId).orElse(null);

        if (product != null) {
            cart.getItems().removeIf(item -> item.getProduct().equals(product));
            cartRepository.save(cart);
        }
    }

    @Override
    public void updateProductQuantityInCart(User user, String productId, int quantity) {
        Cart cart = getCartForUser(user);
        Product product = productRepository.findById(productId).orElse(null);

        if (product != null) {
            for (CartItem item : cart.getItems()) {
                if (item.getProduct().equals(product)) {
                    item.setQuantity(quantity);
                    cartItemRepository.save(item);
                    break;
                }
            }
        }
    }
}
