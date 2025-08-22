package com.shashi.service.product;

import com.shashi.dataaccess.entity.ProductEntity;
import com.shashi.dataaccess.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProductServiceImpl implements ProductService {

    @Autowired
    private ProductRepository productRepository;

    @Override
    public ProductEntity addProduct(ProductEntity product) {
        return productRepository.save(product);
    }

    @Override
    public ProductEntity updateProduct(ProductEntity product) {
        return productRepository.save(product);
    }

    @Override
    public void deleteProduct(String productId) {
        productRepository.deleteById(productId);
    }

    @Override
    public Optional<ProductEntity> getProductById(String productId) {
        return productRepository.findById(productId);
    }

    @Override
    public List<ProductEntity> getAllProducts() {
        return productRepository.findAll();
    }

    @Override
    public void updateProductQuantity(String productId, Integer quantity) {
        Optional<ProductEntity> productOptional = productRepository.findById(productId);
        if (productOptional.isPresent()) {
            ProductEntity product = productOptional.get();
            product.setPquantity(quantity);
            productRepository.save(product);
        }
    }
}
