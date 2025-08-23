
package com.shoppingcart;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, String> {

    @Query("SELECT new com.shoppingcart.Product(p.pid, p.image, p.info, p.name, p.price, p.quantity) FROM Product p")
    List<Product> findAllProducts();

    List<Product> findByNameContainingIgnoreCase(String keyword);

    

    
}
