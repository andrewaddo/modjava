package com.shashi.webapp.service;

import com.shashi.dataaccess.entity.Product;
import com.shashi.dataaccess.repository.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class ProductServiceTest {

    @Mock
    private ProductRepository productRepository;

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
    void testSaveProduct() {
        Product product = new Product("P001", "Laptop", "Electronics", "Info1", BigDecimal.valueOf(1200.00), 10, null);
        when(productRepository.save(product)).thenReturn(product);

        Product result = productService.saveProduct(product);

        assertNotNull(result);
        assertEquals("P001", result.getPid());
        verify(productRepository, times(1)).save(product);
    }

    @Test
    void testDeleteProduct() {
        doNothing().when(productRepository).deleteById("P001");

        productService.deleteProduct("P001");

        verify(productRepository, times(1)).deleteById("P001");
    }

    @Test
    void testUpdateProduct_Success() {
        Product existingProduct = new Product("P001", "Laptop", "Electronics", "Info1", BigDecimal.valueOf(1200.00), 10, null);
        Product updatedProduct = new Product("P001", "Laptop Pro", "Electronics", "Updated Info", BigDecimal.valueOf(1300.00), 8, null);

        when(productRepository.existsById("P001")).thenReturn(true);
        when(productRepository.save(updatedProduct)).thenReturn(updatedProduct);

        boolean result = productService.updateProduct(updatedProduct);

        assertTrue(result);
        verify(productRepository, times(1)).existsById("P001");
        verify(productRepository, times(1)).save(updatedProduct);
    }

    @Test
    void testUpdateProduct_NotFound() {
        Product nonExistentProduct = new Product("P003", "Tablet", "Electronics", "Info3", BigDecimal.valueOf(300.00), 5, null);

        when(productRepository.existsById("P003")).thenReturn(false);

        boolean result = productService.updateProduct(nonExistentProduct);

        assertFalse(result);
        verify(productRepository, times(1)).existsById("P003");
        verify(productRepository, never()).save(any(Product.class));
    }
}
