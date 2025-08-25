package com.shoppingcart;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional; // Import Transactional

import java.io.InputStream;
import java.util.List;
import jakarta.mail.MessagingException; // Import MessagingException

@Service
public class ProductServiceImpl implements ProductService {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private EmailService emailService; // Inject EmailService

    @Autowired
    private DemandService demandService; // Inject DemandService

    @Override
    public Product addProduct(Product product, String imageUrl) {
        product.setImage(imageUrl);
        product.setPid(UUID.randomUUID().toString());
        return productRepository.save(product);
    }

    @Override
    @Transactional // Add Transactional annotation
    public Product updateProduct(String id, Product product) {
        Product existingProduct = productRepository.findById(id).orElse(null);
        if (existingProduct != null) {
            int oldQuantity = existingProduct.getQuantity(); // Get old quantity
            existingProduct.setName(product.getName());
            existingProduct.setInfo(product.getInfo());
            existingProduct.setPrice(product.getPrice());
            existingProduct.setQuantity(product.getQuantity());
            
            Product updatedProduct = productRepository.save(existingProduct);

            // Check for back-in-stock notifications
            if (oldQuantity == 0 && updatedProduct.getQuantity() > 0) {
                List<Demand> demands = demandService.getDemandsByProdid(updatedProduct.getPid());
                for (Demand demand : demands) {
                    try {
                        emailService.sendProductAvailableEmail(demand.getUser().getEmail(), demand.getUser().getName(), updatedProduct.getName(), updatedProduct.getPid());
                    } catch (MessagingException e) {
                        e.printStackTrace(); // Log the exception
                    }
                }
                demandService.deleteDemands(demands); // Remove fulfilled demands
            }
            return updatedProduct;
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