package com.shashi.webapp.service;

import com.shashi.dataaccess.entity.*;
import com.shashi.dataaccess.repository.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class OrderServiceTest {

    private OrdersRepository ordersRepository = mock(OrdersRepository.class);
    private TransactionsRepository transactionsRepository = mock(TransactionsRepository.class);
    private UsercartRepository usercartRepository = mock(UsercartRepository.class);
    private ProductRepository productRepository = mock(ProductRepository.class);
    private EmailService emailService = mock(EmailService.class);
    private UserService userService = mock(UserService.class);

    private OrderService orderService;

    private final String TEST_USERNAME = "test@example.com";
    private final String TEST_PRODID = "P001";
    private final String TEST_PRODID_2 = "P002";

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        orderService = new OrderService();
        orderService.setOrdersRepository(ordersRepository);
        orderService.setTransactionsRepository(transactionsRepository);
        orderService.setUsercartRepository(usercartRepository);
        orderService.setProductRepository(productRepository);
        orderService.setEmailService(emailService);
        orderService.setUserService(userService);
    }

    @Test
    void testPlaceOrder_Success() {
        Usercart cartItem1 = new Usercart(new UsercartId(TEST_USERNAME, TEST_PRODID), 2);
        Usercart cartItem2 = new Usercart(new UsercartId(TEST_USERNAME, TEST_PRODID_2), 1);
        List<Usercart> cartItems = Arrays.asList(cartItem1, cartItem2);

        Product product1 = new Product(TEST_PRODID, "Product 1", "Type", "Info", BigDecimal.valueOf(100.00), 10, null);
        Product product2 = new Product(TEST_PRODID_2, "Product 2", "Type", "Info", BigDecimal.valueOf(50.00), 5, null);

        when(usercartRepository.findById_Username(TEST_USERNAME)).thenReturn(cartItems);
        when(productRepository.findById(TEST_PRODID)).thenReturn(Optional.of(product1));
        when(productRepository.findById(TEST_PRODID_2)).thenReturn(Optional.of(product2));
        when(ordersRepository.save(any(Orders.class))).thenAnswer(invocation -> invocation.getArgument(0));
        when(transactionsRepository.save(any(Transactions.class))).thenAnswer(invocation -> invocation.getArgument(0));
        when(userService.getUserByEmail(TEST_USERNAME)).thenReturn(new User(TEST_USERNAME, "Test User", null, null, null, null));

        String orderId = orderService.placeOrder(TEST_USERNAME, BigDecimal.valueOf(250.00));

        assertNotNull(orderId);
        verify(ordersRepository, times(2)).save(any(Orders.class));
        verify(transactionsRepository, times(1)).save(any(Transactions.class));
        verify(usercartRepository, times(1)).deleteAll(cartItems);
        verify(productRepository, times(1)).save(product1);
        verify(productRepository, times(1)).save(product2);
    }

    @Test
    void testPlaceOrder_EmptyCart() {
        when(usercartRepository.findById_Username(TEST_USERNAME)).thenReturn(Collections.emptyList());

        String orderId = orderService.placeOrder(TEST_USERNAME, BigDecimal.ZERO);

        assertNull(orderId);
        verify(ordersRepository, never()).save(any(Orders.class));
        verify(transactionsRepository, never()).save(any(Transactions.class));
        verify(usercartRepository, never()).deleteAll(anyList());
    }

    @Test
    void testGetOrdersByOrderId() {
        OrderId orderId1 = new OrderId("ORDER1", TEST_PRODID);
        Orders order1 = new Orders(orderId1, 1, BigDecimal.valueOf(100.00), 0);
        when(ordersRepository.findById_Orderid("ORDER1")).thenReturn(Arrays.asList(order1));

        List<Orders> result = orderService.getOrdersByOrderId("ORDER1");

        assertNotNull(result);
        assertEquals(1, result.size());
        verify(ordersRepository, times(1)).findById_Orderid("ORDER1");
    }

    @Test
    void testGetTransactionsByUsername() {
        Transactions transaction = new Transactions("TRANS1", TEST_USERNAME, LocalDateTime.now(), BigDecimal.valueOf(150.00));
        when(transactionsRepository.findByUsername(TEST_USERNAME)).thenReturn(Arrays.asList(transaction));

        List<Transactions> result = orderService.getTransactionsByUsername(TEST_USERNAME);

        assertNotNull(result);
        assertEquals(1, result.size());
        verify(transactionsRepository, times(1)).findByUsername(TEST_USERNAME);
    }

    @Test
    void testGetAllOrders() {
        Orders order1 = new Orders(new OrderId("ORDER1", TEST_PRODID), 1, BigDecimal.valueOf(100.00), 0);
        Orders order2 = new Orders(new OrderId("ORDER2", TEST_PRODID_2), 2, BigDecimal.valueOf(200.00), 1);
        when(ordersRepository.findAll()).thenReturn(Arrays.asList(order1, order2));

        List<Orders> result = orderService.getAllOrders();

        assertNotNull(result);
        assertEquals(2, result.size());
        verify(ordersRepository, times(1)).findAll();
    }

    @Test
    void testUpdateOrderStatus_Success() {
        OrderId id = new OrderId("ORDER1", TEST_PRODID);
        Orders order = new Orders(id, 1, BigDecimal.valueOf(100.00), 0);
        User user = new User(TEST_USERNAME, "Test User", null, null, null, null);
        Transactions transaction = new Transactions("ORDER1", TEST_USERNAME, LocalDateTime.now(), BigDecimal.valueOf(100.00));

        when(ordersRepository.findById(id)).thenReturn(Optional.of(order));
        when(ordersRepository.save(any(Orders.class))).thenReturn(order);
        when(transactionsRepository.findById("ORDER1")).thenReturn(Optional.of(transaction));
        when(userService.getUserByEmail(TEST_USERNAME)).thenReturn(user);
        doNothing().when(emailService).sendShippingUpdate(anyString(), anyString(), anyString(), anyString());

        boolean result = orderService.updateOrderStatus("ORDER1", TEST_PRODID, 1);

        assertTrue(result);
        assertEquals(1, order.getShipped());
        verify(ordersRepository, times(1)).findById(id);
        verify(ordersRepository, times(1)).save(order);
        verify(transactionsRepository, times(1)).findById("ORDER1");
        verify(userService, times(1)).getUserByEmail(TEST_USERNAME);
        verify(emailService, times(1)).sendShippingUpdate(user.getEmail(), "ORDER1", TEST_PRODID, user.getName());
    }

    @Test
    void testUpdateOrderStatus_NotFound() {
        OrderId id = new OrderId("NONEXISTENT", TEST_PRODID);
        when(ordersRepository.findById(id)).thenReturn(Optional.empty());

        boolean result = orderService.updateOrderStatus("NONEXISTENT", TEST_PRODID, 1);

        assertFalse(result);
        verify(ordersRepository, times(1)).findById(id);
        verify(ordersRepository, never()).save(any(Orders.class));
    }

    @Test
    void testGetOrdersByShippedStatus() {
        Orders order1 = new Orders(new OrderId("ORDER1", TEST_PRODID), 1, BigDecimal.valueOf(100.00), 0);
        Orders order2 = new Orders(new OrderId("ORDER2", TEST_PRODID_2), 2, BigDecimal.valueOf(200.00), 1);
        Orders order3 = new Orders(new OrderId("ORDER3", TEST_PRODID), 1, BigDecimal.valueOf(100.00), 1);
        when(ordersRepository.findAll()).thenReturn(Arrays.asList(order1, order2, order3));

        List<Orders> shippedOrders = orderService.getOrdersByShippedStatus(1);
        assertNotNull(shippedOrders);
        assertEquals(2, shippedOrders.size());
        assertTrue(shippedOrders.stream().allMatch(o -> o.getShipped() == 1));

        List<Orders> unshippedOrders = orderService.getOrdersByShippedStatus(0);
        assertNotNull(unshippedOrders);
        assertEquals(1, unshippedOrders.size());
        assertTrue(unshippedOrders.stream().allMatch(o -> o.getShipped() == 0));

        verify(ordersRepository, times(2)).findAll();
    }

    @Test
    void testGetOrdersForUser() {
        Transactions transaction1 = new Transactions("ORDER1", TEST_USERNAME, LocalDateTime.now(), BigDecimal.valueOf(150.00));
        Transactions transaction2 = new Transactions("ORDER2", TEST_USERNAME, LocalDateTime.now(), BigDecimal.valueOf(250.00));
        List<Transactions> userTransactions = Arrays.asList(transaction1, transaction2);

        Orders orderA = new Orders(new OrderId("ORDER1", TEST_PRODID), 1, BigDecimal.valueOf(100.00), 0);
        Orders orderB = new Orders(new OrderId("ORDER2", TEST_PRODID_2), 2, BigDecimal.valueOf(200.00), 1);

        when(transactionsRepository.findByUsername(TEST_USERNAME)).thenReturn(userTransactions);
        when(ordersRepository.findById_OrderidIn(Arrays.asList("ORDER1", "ORDER2"))).thenReturn(Arrays.asList(orderA, orderB));

        List<Orders> result = orderService.getOrdersForUser(TEST_USERNAME);

        assertNotNull(result);
        assertEquals(2, result.size());
        verify(transactionsRepository, times(1)).findByUsername(TEST_USERNAME);
        verify(ordersRepository, times(1)).findById_OrderidIn(Arrays.asList("ORDER1", "ORDER2"));
    }
}