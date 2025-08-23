
package com.shoppingcart;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ProductServiceImplTest {

    @Mock
    private ProductRepository productRepository;

    @Mock
    private CategoryRepository categoryRepository;

    @InjectMocks
    private ProductServiceImpl productService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testAddProduct() {
        Product product = new Product();
        product.setName("Test Product");
        product.setInfo("Test Info");
        product.setPrice(10.0);
        product.setQuantity(10);

        Category category = new Category();
        category.setId("cat1");
        category.setName("Test Category");
        product.setCategory(category);

        when(productRepository.save(any(Product.class))).thenReturn(product);

        Product savedProduct = productService.addProduct(product, null);

        assertNotNull(savedProduct.getId());
        assertEquals("Test Product", savedProduct.getName());
        verify(productRepository, times(1)).save(product);
    }

    @Test
    void testUpdateProduct() {
        Product existingProduct = new Product();
        existingProduct.setId("1");
        existingProduct.setName("Old Name");
        existingProduct.setInfo("Old Info");
        existingProduct.setPrice(5.0);
        existingProduct.setQuantity(5);

        Product updatedProduct = new Product();
        updatedProduct.setName("New Name");
        updatedProduct.setInfo("New Info");
        updatedProduct.setPrice(15.0);
        updatedProduct.setQuantity(15);

        Category category = new Category();
        category.setId("cat1");
        category.setName("Test Category");
        updatedProduct.setCategory(category);

        when(productRepository.findById("1")).thenReturn(Optional.of(existingProduct));
        when(productRepository.save(any(Product.class))).thenReturn(updatedProduct);

        Product result = productService.updateProduct("1", updatedProduct);

        assertNotNull(result);
        assertEquals("New Name", result.getName());
        assertEquals(15.0, result.getPrice());
        verify(productRepository, times(1)).save(existingProduct);
    }

    @Test
    void testRemoveProduct() {
        productService.removeProduct("1");

        verify(productRepository, times(1)).deleteById("1");
    }

    @Test
    void testGetAllProducts() {
        Product product1 = new Product();
        product1.setId("1");
        Product product2 = new Product();
        product2.setId("2");

        when(productRepository.findAll()).thenReturn(Arrays.asList(product1, product2));

        List<Product> products = productService.getAllProducts();

        assertEquals(2, products.size());
        verify(productRepository, times(1)).findAll();
    }

    @Test
    void testGetProductsByCategory() {
        Category category = new Category();
        category.setId("cat1");
        category.setName("Test Category");

        Product product1 = new Product();
        product1.setId("1");
        product1.setCategory(category);

        when(categoryRepository.findById("cat1")).thenReturn(Optional.of(category));
        when(productRepository.findByCategory(category)).thenReturn(Arrays.asList(product1));

        List<Product> products = productService.getProductsByCategory("cat1");

        assertEquals(1, products.size());
        assertEquals("1", products.get(0).getId());
        verify(categoryRepository, times(1)).findById("cat1");
        verify(productRepository, times(1)).findByCategory(category);
    }

    @Test
    void testSearchProducts() {
        Product product1 = new Product();
        product1.setId("1");
        product1.setName("Test Product 1");
        Product product2 = new Product();
        product2.setId("2");
        product2.setName("Another Product");

        when(productRepository.findByNameContainingIgnoreCase("test")).thenReturn(Arrays.asList(product1));

        List<Product> products = productService.searchProducts("test");

        assertEquals(1, products.size());
        assertEquals("Test Product 1", products.get(0).getName());
        verify(productRepository, times(1)).findByNameContainingIgnoreCase("test");
    }

    @Test
    void testGetProductById() {
        Product product = new Product();
        product.setId("1");

        when(productRepository.findById("1")).thenReturn(Optional.of(product));

        Product result = productService.getProductById("1");

        assertNotNull(result);
        assertEquals("1", result.getId());
    }

    @Test
    void testGetProductById_NotFound() {
        when(productRepository.findById("1")).thenReturn(Optional.empty());

        Product result = productService.getProductById("1");

        assertNull(result);
    }
}
