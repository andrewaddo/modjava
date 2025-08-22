package com.shashi.service.product;

import com.shashi.dataaccess.entity.ProductEntity;
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

public class ProductServiceImplTest {

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private ProductServiceImpl productService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testAddProduct() {
        ProductEntity product = new ProductEntity();
        product.setPid("P001");
        product.setPname("Test Product");

        when(productRepository.save(product)).thenReturn(product);

        ProductEntity savedProduct = productService.addProduct(product);

        assertNotNull(savedProduct);
        assertEquals("P001", savedProduct.getPid());
        verify(productRepository, times(1)).save(product);
    }

    @Test
    void testUpdateProduct() {
        ProductEntity product = new ProductEntity();
        product.setPid("P001");
        product.setPname("Updated Product");

        when(productRepository.save(product)).thenReturn(product);

        ProductEntity updatedProduct = productService.updateProduct(product);

        assertNotNull(updatedProduct);
        assertEquals("Updated Product", updatedProduct.getPname());
        verify(productRepository, times(1)).save(product);
    }

    @Test
    void testDeleteProduct() {
        String productId = "P001";
        doNothing().when(productRepository).deleteById(productId);

        productService.deleteProduct(productId);

        verify(productRepository, times(1)).deleteById(productId);
    }

    @Test
    void testGetProductById_found() {
        ProductEntity product = new ProductEntity();
        product.setPid("P001");

        when(productRepository.findById("P001")).thenReturn(Optional.of(product));

        Optional<ProductEntity> foundProduct = productService.getProductById("P001");

        assertTrue(foundProduct.isPresent());
        assertEquals("P001", foundProduct.get().getPid());
        verify(productRepository, times(1)).findById("P001");
    }

    @Test
    void testGetProductById_notFound() {
        when(productRepository.findById("P001")).thenReturn(Optional.empty());

        Optional<ProductEntity> foundProduct = productService.getProductById("P001");

        assertFalse(foundProduct.isPresent());
        verify(productRepository, times(1)).findById("P001");
    }

    @Test
    void testGetAllProducts() {
        ProductEntity product1 = new ProductEntity();
        product1.setPid("P001");
        ProductEntity product2 = new ProductEntity();
        product2.setPid("P002");

        List<ProductEntity> products = Arrays.asList(product1, product2);

        when(productRepository.findAll()).thenReturn(products);

        List<ProductEntity> allProducts = productService.getAllProducts();

        assertNotNull(allProducts);
        assertEquals(2, allProducts.size());
        verify(productRepository, times(1)).findAll();
    }

    @Test
    void testUpdateProductQuantity_success() {
        ProductEntity product = new ProductEntity();
        product.setPid("P001");
        product.setPquantity(10);

        when(productRepository.findById("P001")).thenReturn(Optional.of(product));
        when(productRepository.save(product)).thenReturn(product);

        productService.updateProductQuantity("P001", 15);

        assertEquals(15, product.getPquantity());
        verify(productRepository, times(1)).findById("P001");
        verify(productRepository, times(1)).save(product);
    }

    @Test
    void testUpdateProductQuantity_productNotFound() {
        when(productRepository.findById("P001")).thenReturn(Optional.empty());

        productService.updateProductQuantity("P001", 15);

        verify(productRepository, times(1)).findById("P001");
        verify(productRepository, never()).save(any(ProductEntity.class));
    }
}
