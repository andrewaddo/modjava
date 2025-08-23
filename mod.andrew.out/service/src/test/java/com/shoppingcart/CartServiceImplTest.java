
package com.shoppingcart;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

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
}
