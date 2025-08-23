
package com.shoppingcart;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, String> {

    List<Product> findByCategory(Category category);

    List<Product> findByNameContainingIgnoreCase(String name);
}
