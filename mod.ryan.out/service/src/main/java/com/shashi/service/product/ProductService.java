package com.shashi.service.product;

import com.shashi.dataaccess.entity.ProductEntity;

import java.util.List;
import java.util.Optional;

public interface ProductService {
    ProductEntity addProduct(ProductEntity product);
    ProductEntity updateProduct(ProductEntity product);
    void deleteProduct(String productId);
    Optional<ProductEntity> getProductById(String productId);
    List<ProductEntity> getAllProducts();
    void updateProductQuantity(String productId, Integer quantity);
}
