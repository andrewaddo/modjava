package com.shashi.webapp.controller;

import com.shashi.dataaccess.entity.Orders;
import com.shashi.dataaccess.entity.OrderId;
import com.shashi.dataaccess.entity.Product;
import com.shashi.dataaccess.entity.Transactions;
import com.shashi.dataaccess.entity.Usercart;
import com.shashi.dataaccess.entity.UsercartId;
import com.shashi.webapp.config.TestSecurityConfig;
import com.shashi.webapp.service.CartService;
import com.shashi.webapp.service.OrderService;
import com.shashi.webapp.service.ProductService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(OrderController.class)
@Import(TestSecurityConfig.class)
public class OrderControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CartService cartService;

    @MockBean
    private OrderService orderService;

    @MockBean
    private ProductService productService;

    private final String TEST_USERNAME = "user@example.com";
    private final String TEST_PRODID = "P001";
    private final String TEST_ORDERID = "TR12345";

    @Test
    @WithMockUser(username = TEST_USERNAME)
    void testShowCheckout() throws Exception {
        Usercart cartItem = new Usercart(new UsercartId(TEST_USERNAME, TEST_PRODID), 2);
        List<Usercart> cartItems = Arrays.asList(cartItem);
        Product product = new Product(TEST_PRODID, "Test Product", "Type", "Info", BigDecimal.valueOf(100.00), 10, null);

        when(cartService.getCartItemsByUsername(TEST_USERNAME)).thenReturn(cartItems);
        when(productService.getProductById(TEST_PRODID)).thenReturn(Optional.of(product));
        when(cartService.getCartTotal(TEST_USERNAME)).thenReturn(BigDecimal.valueOf(200.00));

        mockMvc.perform(get("/checkout"))
                .andExpect(status().isOk())
                .andExpect(view().name("checkout"))
                .andExpect(model().attributeExists("cartItems", "productsInCart", "cartTotal"));

        verify(cartService, times(1)).getCartItemsByUsername(TEST_USERNAME);
        verify(productService, times(1)).getProductById(TEST_PRODID);
        verify(cartService, times(1)).getCartTotal(TEST_USERNAME);
    }

    @Test
    @WithMockUser(username = TEST_USERNAME)
    void testPlaceOrder_Success() throws Exception {
        BigDecimal totalAmount = BigDecimal.valueOf(200.00);
        when(orderService.placeOrder(TEST_USERNAME, totalAmount)).thenReturn(TEST_ORDERID);

        mockMvc.perform(post("/placeOrder")
                        .param("totalAmount", totalAmount.toString()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/orderConfirmation?orderId=" + TEST_ORDERID));

        verify(orderService, times(1)).placeOrder(TEST_USERNAME, totalAmount);
    }

    @Test
    @WithMockUser(username = TEST_USERNAME)
    void testPlaceOrder_EmptyCart() throws Exception {
        BigDecimal totalAmount = BigDecimal.ZERO;
        when(orderService.placeOrder(TEST_USERNAME, totalAmount)).thenReturn(null);

        mockMvc.perform(post("/placeOrder")
                        .param("totalAmount", totalAmount.toString()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/cartDetails?error=emptyCart"));

        verify(orderService, times(1)).placeOrder(TEST_USERNAME, totalAmount);
    }

    @Test
    @WithMockUser(username = TEST_USERNAME)
    void testOrderConfirmation() throws Exception {
        Orders order = new Orders(new OrderId(TEST_ORDERID, TEST_PRODID), 2, BigDecimal.valueOf(200.00), 0);
        when(orderService.getOrdersByOrderId(TEST_ORDERID)).thenReturn(Arrays.asList(order));

        mockMvc.perform(get("/orderConfirmation")
                        .param("orderId", TEST_ORDERID))
                .andExpect(status().isOk())
                .andExpect(view().name("order-confirmation"))
                .andExpect(model().attributeExists("orderId", "orders"))
                .andExpect(model().attribute("orderId", TEST_ORDERID))
                .andExpect(model().attribute("orders", Arrays.asList(order)));

        verify(orderService, times(1)).getOrdersByOrderId(TEST_ORDERID);
    }

    @Test
    @WithMockUser(username = TEST_USERNAME)
    void testOrderHistory() throws Exception {
        Transactions transaction = new Transactions(TEST_ORDERID, TEST_USERNAME, LocalDateTime.now(), BigDecimal.valueOf(200.00));
        when(orderService.getTransactionsByUsername(TEST_USERNAME)).thenReturn(Arrays.asList(transaction));

        mockMvc.perform(get("/orderHistory"))
                .andExpect(status().isOk())
                .andExpect(view().name("order-history"))
                .andExpect(model().attributeExists("transactions"))
                .andExpect(model().attribute("transactions", Arrays.asList(transaction)));

        verify(orderService, times(1)).getTransactionsByUsername(TEST_USERNAME);
    }

    @Test
    @WithMockUser(username = TEST_USERNAME)
    void testTrackOrder() throws Exception {
        Orders order = new Orders(new OrderId(TEST_ORDERID, TEST_PRODID), 2, BigDecimal.valueOf(200.00), 0);
        when(orderService.getOrdersByOrderId(TEST_ORDERID)).thenReturn(Arrays.asList(order));

        mockMvc.perform(get("/trackOrder")
                        .param("orderId", TEST_ORDERID))
                .andExpect(status().isOk())
                .andExpect(view().name("track-order"))
                .andExpect(model().attributeExists("orderId", "orders"))
                .andExpect(model().attribute("orderId", TEST_ORDERID))
                .andExpect(model().attribute("orders", Arrays.asList(order)));

        verify(orderService, times(1)).getOrdersByOrderId(TEST_ORDERID);
    }
}
