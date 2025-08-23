
package com.shoppingcart;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

public interface CartItemRepository extends JpaRepository<CartItem, String> {
    CartItem findByCartAndProduct(Cart cart, Product product);

    @Transactional
    void deleteByCart(Cart cart);
}
