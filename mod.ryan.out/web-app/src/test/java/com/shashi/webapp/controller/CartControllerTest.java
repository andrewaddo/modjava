package com.shashi.webapp.controller;

import com.shashi.dataaccess.entity.ProductEntity;
import com.shashi.dataaccess.entity.UserCartEntity;
import com.shashi.service.cart.CartService;
import com.shashi.service.product.ProductService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.ui.Model;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class CartControllerTest {

    @Mock
    private CartService cartService;

    @Mock
    private ProductService productService;

    @Mock
    private Model model;

    @InjectMocks
    private CartController cartController;

    private final String TEST_USER_EMAIL = "guest@gmail.com";

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testAddProductToCart() {
        String prodid = "P001";
        Integer quantity = 1;

        when(cartService.addProductToCart(TEST_USER_EMAIL, prodid, quantity)).thenReturn(new UserCartEntity());

        String viewName = cartController.addProductToCart(prodid, quantity);
        assertEquals("redirect:/cart/view", viewName);
        verify(cartService, times(1)).addProductToCart(TEST_USER_EMAIL, prodid, quantity);
    }

    @Test
    void testViewCart_emptyCart() {
        when(cartService.getCartItemsByUsername(TEST_USER_EMAIL)).thenReturn(Collections.emptyList());

        String viewName = cartController.viewCart(model);
        assertEquals("cartDetails", viewName);
        verify(model, times(1)).addAttribute("cartItems", Collections.emptyList());
        verify(model, times(1)).addAttribute("grandTotal", BigDecimal.ZERO);
    }

    @Test
    void testViewCart_withItems() {
        ProductEntity product1 = new ProductEntity();
        product1.setPid("P001");
        product1.setPname("Product One");
        product1.setPprice(new BigDecimal("10.00"));

        ProductEntity product2 = new ProductEntity();
        product2.setPid("P002");
        product2.setPname("Product Two");
        product2.setPprice(new BigDecimal("20.00"));

        UserCartEntity item1 = new UserCartEntity();
        item1.setProdid("P001");
        item1.setQuantity(2);

        UserCartEntity item2 = new UserCartEntity();
        item2.setProdid("P002");
        item2.setQuantity(1);

        List<UserCartEntity> cartItems = Arrays.asList(item1, item2);

        when(cartService.getCartItemsByUsername(TEST_USER_EMAIL)).thenReturn(cartItems);
        when(productService.getProductById("P001")).thenReturn(Optional.of(product1));
        when(productService.getProductById("P002")).thenReturn(Optional.of(product2));

        String viewName = cartController.viewCart(model);
        assertEquals("cartDetails", viewName);

        verify(model, times(1)).addAttribute(eq("cartItems"), anyList());
        verify(model, times(1)).addAttribute("grandTotal", new BigDecimal("40.00"));

        assertEquals("Product One", item1.getPname());
        assertEquals(new BigDecimal("10.00"), item1.getPprice());
        assertEquals("Product Two", item2.getPname());
        assertEquals(new BigDecimal("20.00"), item2.getPprice());
    }

    @Test
    void testUpdateCartItemQuantity_update() {
        String prodid = "P001";
        Integer quantity = 3;

        when(cartService.updateProductQuantityInCart(TEST_USER_EMAIL, prodid, quantity)).thenReturn(new UserCartEntity());

        String viewName = cartController.updateCartItemQuantity(prodid, quantity);
        assertEquals("redirect:/cart/view", viewName);
        verify(cartService, times(1)).updateProductQuantityInCart(TEST_USER_EMAIL, prodid, quantity);
        verify(cartService, never()).removeProductFromCart(anyString(), anyString());
    }

    @Test
    void testUpdateCartItemQuantity_removeIfZero() {
        String prodid = "P001";
        Integer quantity = 0;

        doNothing().when(cartService).removeProductFromCart(TEST_USER_EMAIL, prodid);

        String viewName = cartController.updateCartItemQuantity(prodid, quantity);
        assertEquals("redirect:/cart/view", viewName);
        verify(cartService, times(1)).removeProductFromCart(TEST_USER_EMAIL, prodid);
        verify(cartService, never()).updateProductQuantityInCart(anyString(), anyString(), anyInt());
    }

    @Test
    void testRemoveCartItem() {
        String prodid = "P001";

        doNothing().when(cartService).removeProductFromCart(TEST_USER_EMAIL, prodid);

        String viewName = cartController.removeCartItem(prodid);
        assertEquals("redirect:/cart/view", viewName);
        verify(cartService, times(1)).removeProductFromCart(TEST_USER_EMAIL, prodid);
    }

    @Test
    void testClearCart() {
        doNothing().when(cartService).clearCart(TEST_USER_EMAIL);

        String viewName = cartController.clearCart();
        assertEquals("redirect:/cart/view", viewName);
        verify(cartService, times(1)).clearCart(TEST_USER_EMAIL);
    }
}
