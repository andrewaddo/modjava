
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
    public Cart getCart(User user) {
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
        Cart cart = getCart(user);
        Product product = productRepository.findById(productId).orElse(null);

        if (product != null) {
            CartItem cartItem = cartItemRepository.findByCartAndProduct(cart, product);
            if (cartItem == null) {
                cartItem = new CartItem();
                cartItem.setCart(cart);
                cartItem.setProduct(product);
                cartItem.setQuantity(quantity);
            } else {
                cartItem.setQuantity(cartItem.getQuantity() + quantity);
            }
            cartItemRepository.save(cartItem);
        }
    }

    @Override
    public void removeProductFromCart(User user, String productId) {
        Cart cart = getCart(user);
        Product product = productRepository.findById(productId).orElse(null);

        if (product != null) {
            CartItem cartItem = cartItemRepository.findByCartAndProduct(cart, product);
            if (cartItem != null) {
                cartItemRepository.delete(cartItem);
            }
        }
    }

    @Override
    public void updateProductQuantity(User user, String productId, int quantity) {
        Cart cart = getCart(user);
        Product product = productRepository.findById(productId).orElse(null);

        if (product != null) {
            CartItem cartItem = cartItemRepository.findByCartAndProduct(cart, product);
            if (cartItem != null) {
                cartItem.setQuantity(quantity);
                cartItemRepository.save(cartItem);
            }
        }
    }

    @Override
    public void clearCart(User user) {
        Cart cart = getCart(user);
        cartItemRepository.deleteByCart(cart);
    }
}
