
package com.shoppingcart;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

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
    void testGetProductById() {
        Product product = new Product();
        product.setId("1");

        when(productRepository.findById("1")).thenReturn(Optional.of(product));

        Product result = productService.getProductById("1");

        assertNotNull(result);
        assertEquals("1", result.getId());
    }

    @Test
    void testRemoveProduct() {
        productService.removeProduct("1");

        verify(productRepository, times(1)).deleteById("1");
    }
}
