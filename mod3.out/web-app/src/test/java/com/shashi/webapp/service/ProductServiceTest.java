package com.shashi.webapp.service;

import com.shashi.dataaccess.entity.Product;
import com.shashi.dataaccess.entity.User;
import com.shashi.dataaccess.entity.UserDemand;
import com.shashi.dataaccess.entity.UserDemandId;
import com.shashi.dataaccess.repository.ProductRepository;
import com.shashi.dataaccess.repository.UserDemandRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class ProductServiceTest {

    @Mock
    private ProductRepository productRepository;

    @Mock
    private UserDemandRepository userDemandRepository;

    @Mock
    private EmailService emailService;

    @Mock
    private UserService userService;

    @InjectMocks
    private ProductService productService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetAllProducts() {
        Product product1 = new Product("P001", "Laptop", "Electronics", "Info1", BigDecimal.valueOf(1200.00), 10, null);
        Product product2 = new Product("P002", "Mouse", "Electronics", "Info2", BigDecimal.valueOf(25.00), 50, null);
        List<Product> products = Arrays.asList(product1, product2);

        when(productRepository.findAll()).thenReturn(products);

        List<Product> result = productService.getAllProducts();

        assertNotNull(result);
        assertEquals(2, result.size());
        verify(productRepository, times(1)).findAll();
    }

    @Test
    void testGetProductById_Found() {
        Product product = new Product("P001", "Laptop", "Electronics", "Info1", BigDecimal.valueOf(1200.00), 10, null);
        when(productRepository.findById("P001")).thenReturn(Optional.of(product));

        Optional<Product> result = productService.getProductById("P001");

        assertTrue(result.isPresent());
        assertEquals("P001", result.get().getPid());
        verify(productRepository, times(1)).findById("P001");
    }

    @Test
    void testGetProductById_NotFound() {
        when(productRepository.findById("P003")).thenReturn(Optional.empty());

        Optional<Product> result = productService.getProductById("P003");

        assertFalse(result.isPresent());
        verify(productRepository, times(1)).findById("P003");
    }

    @Test
    void testSaveProduct_NewProduct_NotifyBackInStock() {
        Product newProduct = new Product("P001", "Laptop", "Electronics", "Info1", BigDecimal.valueOf(1200.00), 10, null);
        UserDemand userDemand = new UserDemand(new UserDemandId("user@example.com", "P001"), 1);
        User user = new User("user@example.com", "Test User", null, null, null, null);

        when(productRepository.save(newProduct)).thenReturn(newProduct);
        when(userDemandRepository.findById_Prodid("P001")).thenReturn(Arrays.asList(userDemand));
        when(userService.getUserByEmail("user@example.com")).thenReturn(user);
        doNothing().when(emailService).sendBackInStockNotification(anyString(), anyString(), anyString());

        Product result = productService.saveProduct(newProduct);

        assertNotNull(result);
        assertEquals("P001", result.getPid());
        verify(productRepository, times(1)).save(newProduct);
        verify(userDemandRepository, times(1)).findById_Prodid("P001");
        verify(userService, times(1)).getUserByEmail("user@example.com");
        verify(emailService, times(1)).sendBackInStockNotification("user@example.com", "Laptop", "Test User");
        verify(userDemandRepository, times(1)).delete(userDemand);
    }

    @Test
    void testSaveProduct_NewProduct_NoDemand() {
        Product newProduct = new Product("P001", "Laptop", "Electronics", "Info1", BigDecimal.valueOf(1200.00), 10, null);

        when(productRepository.save(newProduct)).thenReturn(newProduct);
        when(userDemandRepository.findById_Prodid("P001")).thenReturn(Collections.emptyList());

        Product result = productService.saveProduct(newProduct);

        assertNotNull(result);
        assertEquals("P001", result.getPid());
        verify(productRepository, times(1)).save(newProduct);
        verify(userDemandRepository, times(1)).findById_Prodid("P001");
        verify(userService, never()).getUserByEmail(anyString());
        verify(emailService, never()).sendBackInStockNotification(anyString(), anyString(), anyString());
        verify(userDemandRepository, never()).delete(any(UserDemand.class));
    }

    @Test
    void testDeleteProduct() {
        doNothing().when(productRepository).deleteById("P001");

        productService.deleteProduct("P001");

        verify(productRepository, times(1)).deleteById("P001");
    }

    @Test
    void testUpdateProduct_QuantityChange_NotifyBackInStock() {
        Product productToUpdate = new Product("P001", "Laptop", "Electronics", "Info1", BigDecimal.valueOf(1200.00), 0, null);
        UserDemand userDemand = new UserDemand(new UserDemandId("user@example.com", "P001"), 1);
        User user = new User("user@example.com", "Test User", null, null, null, null);

        when(productRepository.findById("P001")).thenReturn(Optional.of(productToUpdate));
        when(productRepository.save(any(Product.class))).thenAnswer(invocation -> {
            Product savedProduct = invocation.getArgument(0);
            savedProduct.setPquantity(8); // Simulate quantity update
            return savedProduct;
        });
        when(userDemandRepository.findById_Prodid("P001")).thenReturn(Arrays.asList(userDemand));
        when(userService.getUserByEmail("user@example.com")).thenReturn(user);
        doNothing().when(emailService).sendBackInStockNotification(anyString(), anyString(), anyString());

        boolean result = productService.updateProduct(productToUpdate);

        assertTrue(result);
        verify(productRepository, times(1)).findById("P001");
        verify(productRepository, times(1)).save(any(Product.class));
        verify(userDemandRepository, times(1)).findById_Prodid("P001");
        verify(userService, times(1)).getUserByEmail("user@example.com");
        verify(emailService, times(1)).sendBackInStockNotification("user@example.com", "Laptop", "Test User");
        verify(userDemandRepository, times(1)).delete(userDemand);
    }

    @Test
    void testUpdateProduct_NoQuantityChange_NoNotification() {
        Product existingProduct = new Product("P001", "Laptop", "Electronics", "Info1", BigDecimal.valueOf(1200.00), 10, null);
        Product updatedProduct = new Product("P001", "Laptop Pro", "Electronics", "Updated Info", BigDecimal.valueOf(1300.00), 8, null);

        when(productRepository.findById("P001")).thenReturn(Optional.of(existingProduct));
        when(productRepository.save(any(Product.class))).thenReturn(updatedProduct);

        boolean result = productService.updateProduct(updatedProduct);

        assertTrue(result);
        verify(productRepository, times(1)).findById("P001");
        verify(productRepository, times(1)).save(any(Product.class));
        verify(userDemandRepository, never()).findById_Prodid(anyString());
        verify(userService, never()).getUserByEmail(anyString());
        verify(emailService, never()).sendBackInStockNotification(anyString(), anyString(), anyString());
        verify(userDemandRepository, never()).delete(any(UserDemand.class));
    }

    @Test
    void testUpdateProduct_NotFound() {
        Product nonExistentProduct = new Product("P003", "Tablet", "Electronics", "Info3", BigDecimal.valueOf(300.00), 5, null);

        when(productRepository.findById("P003")).thenReturn(Optional.empty());

        boolean result = productService.updateProduct(nonExistentProduct);

        assertFalse(result);
        verify(productRepository, times(1)).findById("P003");
        verify(productRepository, never()).save(any(Product.class));
        verify(userDemandRepository, never()).findById_Prodid(anyString());
    }
}
