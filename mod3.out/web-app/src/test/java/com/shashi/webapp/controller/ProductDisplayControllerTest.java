package com.shashi.webapp.controller;

import com.shashi.dataaccess.entity.Product;
import com.shashi.webapp.config.TestSecurityConfig;
import com.shashi.webapp.service.ProductService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ProductDisplayController.class)
@Import(TestSecurityConfig.class)
public class ProductDisplayControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProductService productService;

    @Test
    void testListAllProducts() throws Exception {
        Product product1 = new Product("P001", "Laptop", "Electronics", "Info1", BigDecimal.valueOf(1200.00), 10, null);
        Product product2 = new Product("P002", "Mouse", "Electronics", "Info2", BigDecimal.valueOf(25.00), 50, null);
        List<Product> products = Arrays.asList(product1, product2);

        when(productService.getAllProducts()).thenReturn(products);

        mockMvc.perform(get("/products"))
                .andExpect(status().isOk())
                .andExpect(view().name("product-list-user"))
                .andExpect(model().attributeExists("products"))
                .andExpect(model().attribute("products", products));

        verify(productService, times(1)).getAllProducts();
    }
}
