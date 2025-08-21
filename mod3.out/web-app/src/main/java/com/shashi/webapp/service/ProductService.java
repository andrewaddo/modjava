package com.shashi.webapp.service;

import com.shashi.dataaccess.entity.Product;
import com.shashi.dataaccess.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;

    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    public Optional<Product> getProductById(String pid) {
        return productRepository.findById(pid);
    }

    public Product saveProduct(Product product) {
        return productRepository.save(product);
    }

    public void deleteProduct(String pid) {
        productRepository.deleteById(pid);
    }

    public boolean updateProduct(Product product) {
        if (productRepository.existsById(product.getPid())) {
            productRepository.save(product);
            return true;
        }
        return false;
    }
}
