
package com.shoppingcart;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CartServiceImplTest {

    @Mock
    private CartRepository cartRepository;

    @Mock
    private CartItemRepository cartItemRepository;

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private CartServiceImpl cartService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testAddProductToCart() {
        User user = new User();
        user.setEmail("test@test.com");

        Product product = new Product();
        product.setId("1");

        Cart cart = new Cart();
        cart.setUser(user);

        when(cartRepository.findByUser(user)).thenReturn(cart);
        when(productRepository.findById("1")).thenReturn(java.util.Optional.of(product));

        cartService.addProductToCart(user, "1", 1);

        verify(cartItemRepository, times(1)).save(any(CartItem.class));
    }

    @Test
    void testRemoveProductFromCart() {
        User user = new User();
        user.setEmail("test@test.com");

        Product product = new Product();
        product.setId("1");

        Cart cart = new Cart();
        cart.setUser(user);

        CartItem cartItem = new CartItem();
        cartItem.setCart(cart);
        cartItem.setProduct(product);

        when(cartRepository.findByUser(user)).thenReturn(cart);
        when(cartItemRepository.findByCartAndProduct(cart, product)).thenReturn(cartItem);
        when(productRepository.findById("1")).thenReturn(Optional.of(product));

        cartService.removeProductFromCart(user, "1");

        verify(cartItemRepository, times(1)).delete(cartItem);
    }

    @Test
    void testUpdateProductQuantity() {
        User user = new User();
        user.setEmail("test@test.com");

        Product product = new Product();
        product.setId("1");

        Cart cart = new Cart();
        cart.setUser(user);

        CartItem cartItem = new CartItem();
        cartItem.setCart(cart);
        cartItem.setProduct(product);
        cartItem.setQuantity(1);

        when(cartRepository.findByUser(user)).thenReturn(cart);
        when(cartItemRepository.findByCartAndProduct(cart, product)).thenReturn(cartItem);
        when(productRepository.findById("1")).thenReturn(Optional.of(product));

        cartService.updateProductQuantity(user, "1", 5);

        assertEquals(5, cartItem.getQuantity());
        verify(cartItemRepository, times(1)).save(cartItem);
    }

    @Test
    void testGetCart() {
        User user = new User();
        user.setEmail("test@test.com");

        Cart cart = new Cart();
        cart.setUser(user);

        when(cartRepository.findByUser(user)).thenReturn(cart);

        Cart result = cartService.getCart(user);

        assertNotNull(result);
        assertEquals(user, result.getUser());
    }

    @Test
    void testClearCart() {
        User user = new User();
        user.setEmail("test@test.com");

        Cart cart = new Cart();
        cart.setUser(user);

        when(cartRepository.findByUser(user)).thenReturn(cart);

        cartService.clearCart(user);

        verify(cartItemRepository, times(1)).deleteByCart(cart);
    }
}
