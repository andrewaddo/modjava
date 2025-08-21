package com.shashi.webapp.service;

import com.shashi.dataaccess.entity.Product;
import com.shashi.dataaccess.entity.UserDemand;
import com.shashi.dataaccess.entity.User;
import com.shashi.dataaccess.repository.ProductRepository;
import com.shashi.dataaccess.repository.UserDemandRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private UserDemandRepository userDemandRepository;

    @Autowired
    private EmailService emailService;

    @Autowired
    private UserService userService;

    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    public Optional<Product> getProductById(String pid) {
        return productRepository.findById(pid);
    }

    public Product saveProduct(Product product) {
        Product savedProduct = productRepository.save(product);
        // Check for back-in-stock notification if quantity > 0
        if (savedProduct.getPquantity() > 0) {
            notifyBackInStock(savedProduct);
        }
        return savedProduct;
    }

    public void deleteProduct(String pid) {
        productRepository.deleteById(pid);
    }

    public boolean updateProduct(Product product) {
        Optional<Product> existingProductOptional = productRepository.findById(product.getPid());
        if (existingProductOptional.isPresent()) {
            Product existingProduct = existingProductOptional.get();
            // Update product details
            existingProduct.setPname(product.getPname());
            existingProduct.setPtype(product.getPtype());
            existingProduct.setPinfo(product.getPinfo());
            existingProduct.setPprice(product.getPprice());
            existingProduct.setPquantity(product.getPquantity());
            if (product.getImage() != null) {
                existingProduct.setImage(product.getImage());
            }
            Product updatedProduct = productRepository.save(existingProduct);

            // Check for back-in-stock notification if quantity changed from 0 to > 0
            if (existingProduct.getPquantity() == 0 && updatedProduct.getPquantity() > 0) {
                notifyBackInStock(updatedProduct);
            }
            return true;
        }
        return false;
    }

    private void notifyBackInStock(Product product) {
        List<UserDemand> demands = userDemandRepository.findById_Prodid(product.getPid());
        for (UserDemand demand : demands) {
            User user = userService.getUserByEmail(demand.getId().getUsername());
            if (user != null) {
                emailService.sendBackInStockNotification(user.getEmail(), product.getPname(), user.getName());
            }
            userDemandRepository.delete(demand); // Remove demand after notification
        }
    }
}