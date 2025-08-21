package com.shashi.webapp.controller;

import com.shashi.dataaccess.entity.Product;
import com.shashi.webapp.config.TestSecurityConfig;
import com.shashi.webapp.service.ProductService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ProductController.class)
@Import(TestSecurityConfig.class)
public class ProductControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProductService productService;

    @Test
    void testListProducts() throws Exception {
        Product product1 = new Product("P001", "Laptop", "Electronics", "Info1", BigDecimal.valueOf(1200.00), 10, null);
        Product product2 = new Product("P002", "Mouse", "Electronics", "Info2", BigDecimal.valueOf(25.00), 50, null);
        when(productService.getAllProducts()).thenReturn(Arrays.asList(product1, product2));

        mockMvc.perform(get("/admin/products/list"))
                .andExpect(status().isOk())
                .andExpect(view().name("admin/product-list"))
                .andExpect(model().attributeExists("products"))
                .andExpect(model().attribute("products", Arrays.asList(product1, product2)));

        verify(productService, times(1)).getAllProducts();
    }

    @Test
    void testShowAddProductForm() throws Exception {
        mockMvc.perform(get("/admin/products/add"))
                .andExpect(status().isOk())
                .andExpect(view().name("admin/add-product"))
                .andExpect(model().attributeExists("product"));
    }

    @Test
    void testAddProduct() throws Exception {
        Product newProduct = new Product(null, "New Product", "Type", "Info", BigDecimal.valueOf(100.00), 5, null);
        MockMultipartFile file = new MockMultipartFile("productImage", "test.jpg", MediaType.IMAGE_JPEG_VALUE, "test image content".getBytes());

        when(productService.saveProduct(any(Product.class))).thenReturn(newProduct);

        mockMvc.perform(multipart("/admin/products/add")
                        .file(file)
                        .flashAttr("product", newProduct))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/admin/products/list"));

        verify(productService, times(1)).saveProduct(any(Product.class));
    }

    @Test
    void testShowEditProductForm_Found() throws Exception {
        Product existingProduct = new Product("P001", "Laptop", "Electronics", "Info1", BigDecimal.valueOf(1200.00), 10, null);
        when(productService.getProductById("P001")).thenReturn(Optional.of(existingProduct));

        mockMvc.perform(get("/admin/products/edit/P001"))
                .andExpect(status().isOk())
                .andExpect(view().name("admin/edit-product"))
                .andExpect(model().attributeExists("product"))
                .andExpect(model().attribute("product", existingProduct));

        verify(productService, times(1)).getProductById("P001");
    }

    @Test
    void testShowEditProductForm_NotFound() throws Exception {
        when(productService.getProductById("P003")).thenReturn(Optional.empty());

        mockMvc.perform(get("/admin/products/edit/P003"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/admin/products/list"));

        verify(productService, times(1)).getProductById("P003");
    }

    @Test
    void testEditProduct() throws Exception {
        Product updatedProduct = new Product("P001", "Laptop Pro", "Electronics", "Updated Info", BigDecimal.valueOf(1300.00), 8, null);
        MockMultipartFile file = new MockMultipartFile("productImage", "new_test.jpg", MediaType.IMAGE_JPEG_VALUE, "new image content".getBytes());

        when(productService.updateProduct(any(Product.class))).thenReturn(true);
        when(productService.getProductById("P001")).thenReturn(Optional.of(updatedProduct)); // For retaining image if no new one uploaded

        mockMvc.perform(multipart("/admin/products/edit")
                        .file(file)
                        .flashAttr("product", updatedProduct))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/admin/products/list"));

        verify(productService, times(1)).updateProduct(any(Product.class));
    }

    @Test
    void testDeleteProduct() throws Exception {
        doNothing().when(productService).deleteProduct("P001");

        mockMvc.perform(get("/admin/products/delete/P001"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/admin/products/list"));

        verify(productService, times(1)).deleteProduct("P001");
    }

    @Test
    void testShowProductImage_Found() throws Exception {
        byte[] imageData = "image_data".getBytes();
        Product product = new Product("P001", "Laptop", "Electronics", "Info1", BigDecimal.valueOf(1200.00), 10, imageData);
        when(productService.getProductById("P001")).thenReturn(Optional.of(product));

        mockMvc.perform(get("/admin/products/image/P001"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_OCTET_STREAM))
                .andExpect(content().bytes(imageData));

        verify(productService, times(1)).getProductById("P001");
    }

    @Test
    void testShowProductImage_NotFound() throws Exception {
        when(productService.getProductById("P003")).thenReturn(Optional.empty());

        mockMvc.perform(get("/admin/products/image/P003"))
                .andExpect(status().isOk())
                .andExpect(content().bytes(new byte[0])); // Expect empty byte array

        verify(productService, times(1)).getProductById("P003");
    }
}
