
package com.shoppingcart;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List; // Added import

public interface UserCartItemRepository extends JpaRepository<UserCartItem, UserCartItemPK> {

    List<UserCartItem> findByUsername(String username); // Changed method signature
}
