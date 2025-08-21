package com.shashi.webapp.controller;

import com.shashi.dataaccess.entity.Orders;
import com.shashi.dataaccess.entity.OrderId;
import com.shashi.webapp.config.TestSecurityConfig;
import com.shashi.webapp.service.OrderService;
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

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AdminOrderController.class)
@Import(TestSecurityConfig.class)
@WithMockUser(roles = "ADMIN") // Simulate an admin user
public class AdminOrderControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private OrderService orderService;

    private final String TEST_ORDERID = "TR12345";
    private final String TEST_PRODID = "P001";

    @Test
    void testListAllOrders() throws Exception {
        Orders order1 = new Orders(new OrderId(TEST_ORDERID, TEST_PRODID), 2, BigDecimal.valueOf(200.00), 0);
        List<Orders> allOrders = Arrays.asList(order1);
        when(orderService.getAllOrders()).thenReturn(allOrders);

        mockMvc.perform(get("/admin/orders/list"))
                .andExpect(status().isOk())
                .andExpect(view().name("admin/order-list"))
                .andExpect(model().attributeExists("orders"))
                .andExpect(model().attribute("orders", allOrders));

        verify(orderService, times(1)).getAllOrders();
    }

    @Test
    void testListShippedOrders() throws Exception {
        Orders order1 = new Orders(new OrderId(TEST_ORDERID, TEST_PRODID), 2, BigDecimal.valueOf(200.00), 1);
        List<Orders> shippedOrders = Arrays.asList(order1);
        when(orderService.getOrdersByShippedStatus(1)).thenReturn(shippedOrders);

        mockMvc.perform(get("/admin/orders/shipped"))
                .andExpect(status().isOk())
                .andExpect(view().name("admin/order-list"))
                .andExpect(model().attributeExists("orders", "status"))
                .andExpect(model().attribute("orders", shippedOrders))
                .andExpect(model().attribute("status", "Shipped"));

        verify(orderService, times(1)).getOrdersByShippedStatus(1);
    }

    @Test
    void testListUnshippedOrders() throws Exception {
        Orders order1 = new Orders(new OrderId(TEST_ORDERID, TEST_PRODID), 2, BigDecimal.valueOf(200.00), 0);
        List<Orders> unshippedOrders = Arrays.asList(order1);
        when(orderService.getOrdersByShippedStatus(0)).thenReturn(unshippedOrders);

        mockMvc.perform(get("/admin/orders/unshipped"))
                .andExpect(status().isOk())
                .andExpect(view().name("admin/order-list"))
                .andExpect(model().attributeExists("orders", "status"))
                .andExpect(model().attribute("orders", unshippedOrders))
                .andExpect(model().attribute("status", "Unshipped"));

        verify(orderService, times(1)).getOrdersByShippedStatus(0);
    }

    @Test
    void testUpdateOrderStatus() throws Exception {
        when(orderService.updateOrderStatus(TEST_ORDERID, TEST_PRODID, 1)).thenReturn(true);

        mockMvc.perform(post("/admin/orders/updateStatus")
                        .param("orderId", TEST_ORDERID)
                        .param("prodid", TEST_PRODID)
                        .param("shipped", "1"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/admin/orders/list"));

        verify(orderService, times(1)).updateOrderStatus(TEST_ORDERID, TEST_PRODID, 1);
    }
}
