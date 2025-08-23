
package com.shoppingcart;

import java.io.InputStream;
import java.util.List;

public interface ProductService {

    Product addProduct(String name, String type, String info, double price, int quantity, InputStream image);

    Product updateProduct(String id, Product product);

    void removeProduct(String id);

    List<Product> getAllProducts();

    List<Product> getProductsByCategory(String categoryId);

    List<Product> searchProducts(String keyword);

    Product getProductById(String id);
}
