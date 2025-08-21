package com.shashi.webapp.controller;

import com.shashi.dataaccess.entity.Product;
import com.shashi.dataaccess.entity.Usercart;
import com.shashi.dataaccess.entity.UsercartId;
import com.shashi.webapp.config.TestSecurityConfig;
import com.shashi.webapp.service.CartService;
import com.shashi.webapp.service.ProductService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(CartController.class)
@Import(TestSecurityConfig.class)
public class CartControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CartService cartService;

    @MockBean
    private ProductService productService;

    private final String TEST_USERNAME = "user@example.com";
    private final String TEST_PRODID = "P001";

    @Test
    @WithMockUser(username = TEST_USERNAME)
    void testAddToCart() throws Exception {
        doNothing().when(cartService).addProductToCart(TEST_USERNAME, TEST_PRODID, 1);

        mockMvc.perform(post("/addToCart")
                        .param("prodid", TEST_PRODID)
                        .param("quantity", "1"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/cartDetails"));

        verify(cartService, times(1)).addProductToCart(TEST_USERNAME, TEST_PRODID, 1);
    }

    @Test
    @WithMockUser(username = TEST_USERNAME)
    void testViewCart_Empty() throws Exception {
        when(cartService.getCartItemsByUsername(TEST_USERNAME)).thenReturn(Collections.emptyList());
        when(cartService.getCartTotal(TEST_USERNAME)).thenReturn(BigDecimal.ZERO);

        mockMvc.perform(get("/cartDetails"))
                .andExpect(status().isOk())
                .andExpect(view().name("cartDetails"))
                .andExpect(model().attributeExists("cartItems", "cartTotal"))
                .andExpect(model().attribute("cartItems", Collections.emptyList()))
                .andExpect(model().attribute("cartTotal", BigDecimal.ZERO));

        verify(cartService, times(1)).getCartItemsByUsername(TEST_USERNAME);
        verify(cartService, times(1)).getCartTotal(TEST_USERNAME);
    }

    @Test
    @WithMockUser(username = TEST_USERNAME)
    void testViewCart_WithItems() throws Exception {
        Usercart cartItem = new Usercart(new UsercartId(TEST_USERNAME, TEST_PRODID), 2);
        List<Usercart> cartItems = Arrays.asList(cartItem);
        Product product = new Product(TEST_PRODID, "Test Product", "Type", "Info", BigDecimal.valueOf(100.00), 10, null);

        when(cartService.getCartItemsByUsername(TEST_USERNAME)).thenReturn(cartItems);
        when(productService.getProductById(TEST_PRODID)).thenReturn(Optional.of(product));
        when(cartService.getCartTotal(TEST_USERNAME)).thenReturn(BigDecimal.valueOf(200.00));

        mockMvc.perform(get("/cartDetails"))
                .andExpect(status().isOk())
                .andExpect(view().name("cartDetails"))
                .andExpect(model().attributeExists("cartItems", "productsInCart", "cartTotal"))
                .andExpect(model().attribute("cartItems", cartItems))
                .andExpect(model().attribute("cartTotal", BigDecimal.valueOf(200.00)));

        verify(cartService, times(1)).getCartItemsByUsername(TEST_USERNAME);
        verify(productService, times(1)).getProductById(TEST_PRODID);
        verify(cartService, times(1)).getCartTotal(TEST_USERNAME);
    }

    @Test
    @WithMockUser(username = TEST_USERNAME)
    void testUpdateCart() throws Exception {
        doNothing().when(cartService).updateProductQuantity(TEST_USERNAME, TEST_PRODID, 5);

        mockMvc.perform(post("/updateCart")
                        .param("prodid", TEST_PRODID)
                        .param("quantity", "5"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/cartDetails"));

        verify(cartService, times(1)).updateProductQuantity(TEST_USERNAME, TEST_PRODID, 5);
    }

    @Test
    @WithMockUser(username = TEST_USERNAME)
    void testRemoveCart() throws Exception {
        doNothing().when(cartService).removeProductFromCart(TEST_USERNAME, TEST_PRODID);

        mockMvc.perform(get("/removeCart")
                        .param("prodid", TEST_PRODID))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/cartDetails"));

        verify(cartService, times(1)).removeProductFromCart(TEST_USERNAME, TEST_PRODID);
    }
}
