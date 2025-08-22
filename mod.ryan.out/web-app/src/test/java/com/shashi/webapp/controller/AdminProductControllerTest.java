package com.shashi.webapp.controller;

import com.shashi.dataaccess.entity.ProductEntity;
import com.shashi.service.product.ProductService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.ui.Model;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class AdminProductControllerTest {

    @Mock
    private ProductService productService;

    @Mock
    private Model model;

    @InjectMocks
    private AdminProductController adminProductController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testShowAddProductForm() {
        String viewName = adminProductController.showAddProductForm(model);
        assertEquals("admin/addProduct", viewName);
        verify(model, times(1)).addAttribute(eq("product"), any(ProductEntity.class));
    }

    @Test
    void testAddProduct() throws IOException {
        ProductEntity product = new ProductEntity();
        product.setPid("P001");
        MockMultipartFile file = new MockMultipartFile("productImage", "test.jpg", "image/jpeg", "some image".getBytes());

        when(productService.addProduct(any(ProductEntity.class))).thenReturn(product);

        String viewName = adminProductController.addProduct(product, file);
        assertEquals("redirect:/admin/products/view", viewName);
        verify(productService, times(1)).addProduct(product);
        assertEquals(file.getBytes(), product.getImage());
    }

    @Test
    void testViewProducts() {
        ProductEntity product1 = new ProductEntity();
        product1.setPid("P001");
        ProductEntity product2 = new ProductEntity();
        product2.setPid("P002");
        List<ProductEntity> products = Arrays.asList(product1, product2);

        when(productService.getAllProducts()).thenReturn(products);

        String viewName = adminProductController.viewProducts(model);
        assertEquals("admin/viewProduct", viewName);
        verify(model, times(1)).addAttribute("products", products);
    }

    @Test
    void testShowEditProductForm_found() {
        ProductEntity product = new ProductEntity();
        product.setPid("P001");

        when(productService.getProductById("P001")).thenReturn(Optional.of(product));

        String viewName = adminProductController.showEditProductForm("P001", model);
        assertEquals("admin/updateProduct", viewName);
        verify(model, times(1)).addAttribute("product", product);
    }

    @Test
    void testShowEditProductForm_notFound() {
        when(productService.getProductById("P001")).thenReturn(Optional.empty());

        String viewName = adminProductController.showEditProductForm("P001", model);
        assertEquals("redirect:/admin/products/view", viewName);
        verify(model, never()).addAttribute(eq("product"), any());
    }

    @Test
    void testUpdateProduct_withNewImage() throws IOException {
        ProductEntity product = new ProductEntity();
        product.setPid("P001");
        product.setPname("Updated Name");
        MockMultipartFile file = new MockMultipartFile("productImage", "new.jpg", "image/jpeg", "new image".getBytes());

        when(productService.updateProduct(any(ProductEntity.class))).thenReturn(product);

        String viewName = adminProductController.updateProduct(product, file);
        assertEquals("redirect:/admin/products/view", viewName);
        verify(productService, times(1)).updateProduct(product);
        assertEquals(file.getBytes(), product.getImage());
    }

    @Test
    void testUpdateProduct_withoutNewImage_retainsOldImage() throws IOException {
        ProductEntity existingProduct = new ProductEntity();
        existingProduct.setPid("P001");
        existingProduct.setImage("old image".getBytes());

        ProductEntity productToUpdate = new ProductEntity();
        productToUpdate.setPid("P001");
        productToUpdate.setPname("Updated Name");

        MockMultipartFile emptyFile = new MockMultipartFile("productImage", "", "", new byte[0]);

        when(productService.getProductById("P001")).thenReturn(Optional.of(existingProduct));
        when(productService.updateProduct(any(ProductEntity.class))).thenReturn(productToUpdate);

        String viewName = adminProductController.updateProduct(productToUpdate, emptyFile);
        assertEquals("redirect:/admin/products/view", viewName);
        verify(productService, times(1)).getProductById("P001");
        verify(productService, times(1)).updateProduct(productToUpdate);
        assertEquals(existingProduct.getImage(), productToUpdate.getImage());
    }

    @Test
    void testDeleteProduct() {
        String productId = "P001";
        doNothing().when(productService).deleteProduct(productId);

        String viewName = adminProductController.deleteProduct(productId);
        assertEquals("redirect:/admin/products/view", viewName);
        verify(productService, times(1)).deleteProduct(productId);
    }

    @Test
    void testShowStockManagement() {
        ProductEntity product1 = new ProductEntity();
        product1.setPid("P001");
        ProductEntity product2 = new ProductEntity();
        product2.setPid("P002");
        List<ProductEntity> products = Arrays.asList(product1, product2);

        when(productService.getAllProducts()).thenReturn(products);

        String viewName = adminProductController.showStockManagement(model);
        assertEquals("admin/stockProduct", viewName);
        verify(model, times(1)).addAttribute("products", products);
    }

    @Test
    void testUpdateStock() {
        String productId = "P001";
        Integer quantity = 20;
        doNothing().when(productService).updateProductQuantity(productId, quantity);

        String viewName = adminProductController.updateStock(productId, quantity);
        assertEquals("redirect:/admin/products/stock", viewName);
        verify(productService, times(1)).updateProductQuantity(productId, quantity);
    }
}
