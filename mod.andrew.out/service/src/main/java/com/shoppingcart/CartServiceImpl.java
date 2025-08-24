
package com.shoppingcart;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List; // Changed import from ArrayList to List
import java.util.Optional; // Added import

@Service
public class CartServiceImpl implements CartService {

    @Autowired
    private UserCartItemRepository userCartItemRepository; // Changed to UserCartItemRepository

    @Autowired
    private ProductRepository productRepository; // Keep ProductRepository

    // Removed getCart method

    @Override
    @Transactional
    public void addProductToCart(User user, String productId, int quantity) {
        UserCartItemPK id = new UserCartItemPK(user.getEmail(), productId); // Use UserCartItemPK
        Optional<UserCartItem> existingItem = userCartItemRepository.findById(id);

        if (existingItem.isPresent()) {
            UserCartItem item = existingItem.get();
            item.setQuantity(item.getQuantity() + quantity);
            userCartItemRepository.save(item);
        } else {
            UserCartItem newItem = new UserCartItem(user.getEmail(), productId, quantity);
            userCartItemRepository.save(newItem);
        }
    }

    @Override
    @Transactional
    public void removeProductFromCart(User user, String productId) {
        UserCartItemPK id = new UserCartItemPK(user.getEmail(), productId); // Use UserCartItemPK
        userCartItemRepository.deleteById(id);
    }

    @Override
    @Transactional
    public void updateProductQuantity(User user, String productId, int quantity) {
        UserCartItemPK id = new UserCartItemPK(user.getEmail(), productId); // Use UserCartItemPK
        Optional<UserCartItem> existingItem = userCartItemRepository.findById(id);

        if (existingItem.isPresent()) {
            UserCartItem item = existingItem.get();
            item.setQuantity(quantity);
            userCartItemRepository.save(item);
        }
    }

    @Override
    @Transactional
    public void clearCart(User user) {
        List<UserCartItem> userCartItems = userCartItemRepository.findByUsername(user.getEmail());
        userCartItemRepository.deleteAll(userCartItems);
    }

    @Override
    public List<UserCartItem> getAllCartItems(User user) {
        return userCartItemRepository.findByUsername(user.getEmail());
    }
}
