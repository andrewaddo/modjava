
package com.shoppingcart;

import java.io.InputStream;
import java.util.List;

public interface ProductService {

    Product addProduct(Product product, String imageUrl);

    Product updateProduct(String id, Product product);

    void removeProduct(String id);

    List<Product> getAllProducts();

    

    List<Product> searchProducts(String keyword);

    Product getProductById(String id);
}
