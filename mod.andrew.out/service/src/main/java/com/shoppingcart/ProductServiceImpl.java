
package com.shoppingcart;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.util.List;

@Service
public class ProductServiceImpl implements ProductService {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Override
    public Product addProduct(String name, String type, String info, double price, int quantity, InputStream image) {
        // For now, we are not handling the image
        Category category = categoryRepository.findById(type).orElseGet(() -> {
            Category newCategory = new Category();
            newCategory.setId(type);
            newCategory.setName(type);
            return categoryRepository.save(newCategory);
        });

        Product product = new Product();
        product.setName(name);
        product.setCategory(category);
        product.setInfo(info);
        product.setPrice(price);
        product.setQuantity(quantity);

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
            existingProduct.setCategory(product.getCategory());
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
        return productRepository.findAll();
    }

    @Override
    public List<Product> getProductsByCategory(String categoryId) {
        Category category = categoryRepository.findById(categoryId).orElse(null);
        if (category != null) {
            return productRepository.findByCategory(category);
        }
        return null;
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
