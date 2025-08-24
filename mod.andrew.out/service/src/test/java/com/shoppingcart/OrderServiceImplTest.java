
package com.shoppingcart;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class OrderServiceImplTest {

    @Mock
    private OrderRepository orderRepository;

    

    @Mock
    private ProductRepository productRepository; // Added mock for ProductRepository

    @InjectMocks
    private OrderServiceImpl orderService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreateOrder() {
        User user = new User();
        user.setEmail("test@test.com");

        Product product = new Product();
        product.setPid("1");
        product.setPrice(10.0);

        UserCartItem userCartItem = new UserCartItem(user.getEmail(), product.getPid(), 2); // Changed to UserCartItem

        List<UserCartItem> userCartItems = new ArrayList<>(); // Changed to UserCartItem
        userCartItems.add(userCartItem);

        when(productRepository.findById(product.getPid())).thenReturn(Optional.of(product)); // Mock product fetch
        when(orderRepository.save(any(Order.class))).thenAnswer(i -> i.getArguments()[0]);

        Order order = orderService.createOrder(user, userCartItems); // Changed parameter type

        assertNotNull(order);
        assertEquals(1, order.getItems().size());
        assertEquals("PLACED", order.getStatus());
        verify(orderRepository, times(1)).save(any(Order.class));
    }

    @Test
    void testGetOrderById() {
        Order order = new Order();
        order.setId("1");

        when(orderRepository.findById("1")).thenReturn(Optional.of(order));

        Order result = orderService.getOrderById("1");

        assertNotNull(result);
        assertEquals("1", result.getId());
    }

    @Test
    void testGetOrdersByUser() {
        User user = new User();
        user.setEmail("test@test.com");

        Order order1 = new Order();
        order1.setId("1");
        order1.setUser(user);

        Order order2 = new Order();
        order2.setId("2");
        order2.setUser(user);

        when(orderRepository.findByUser(user)).thenReturn(Arrays.asList(order1, order2));

        List<Order> orders = orderService.getOrdersByUser(user);

        assertEquals(2, orders.size());
        verify(orderRepository, times(1)).findByUser(user);
    }

    @Test
    void testUpdateOrderStatus() {
        Order order = new Order();
        order.setId("1");
        order.setStatus("PLACED");

        when(orderRepository.findById("1")).thenReturn(Optional.of(order));
        when(orderRepository.save(any(Order.class))).thenReturn(order);

        Order updatedOrder = orderService.updateOrderStatus("1", "SHIPPED");

        assertNotNull(updatedOrder);
        assertEquals("SHIPPED", updatedOrder.getStatus());
        verify(orderRepository, times(1)).save(order);
    }
}
