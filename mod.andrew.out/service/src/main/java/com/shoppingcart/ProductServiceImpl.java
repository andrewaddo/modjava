
package com.shoppingcart;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.util.List;

@Service
public class ProductServiceImpl implements ProductService {

    @Autowired
    private ProductRepository productRepository;

    

    @Override
    public Product addProduct(Product product, InputStream image) {
        // For now, we are not handling the image
        product.setPid(UUID.randomUUID().toString());
        return productRepository.save(product);
    }

    @Override
    public Product updateProduct(String id, Product product) {
        Product existingProduct = productRepository.findById(id).orElse(null);
        if (existingProduct != null) {
            existingProduct.setName(product.getName());
            existingProduct.setInfo(product.getInfo());
            existingProduct.setPrice(product.getPrice());
            existingProduct.setQuantity(product.getQuantity());
            
            return productRepository.save(existingProduct);
        }
        return null;
    }

    @Override
    public void removeProduct(String id) {
        productRepository.deleteById(id);
    }

    @Override
    public List<Product> getAllProducts() {
        return productRepository.findAllProducts();
    }

    

    @Override
    public List<Product> searchProducts(String keyword) {
        return productRepository.findByNameContainingIgnoreCase(keyword);
    }

    @Override
    public Product getProductById(String id) {
        return productRepository.findById(id).orElse(null);
    }
}
