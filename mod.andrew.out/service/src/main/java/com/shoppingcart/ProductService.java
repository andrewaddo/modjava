
package com.shoppingcart;

import java.io.InputStream;
import java.util.List;

public interface ProductService {

    Product addProduct(Product product, InputStream image);

    Product updateProduct(String id, Product product);

    void removeProduct(String id);

    List<Product> getAllProducts();

    List<Product> getProductsByCategory(String categoryId);

    List<Product> searchProducts(String keyword);

    Product getProductById(String id);
}
