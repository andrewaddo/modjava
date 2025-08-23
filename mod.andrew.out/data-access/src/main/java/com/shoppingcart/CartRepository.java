
package com.shoppingcart;

import org.springframework.data.jpa.repository.JpaRepository;

public interface CartRepository extends JpaRepository<Cart, String> {

    Cart findByUser(User user);
}
