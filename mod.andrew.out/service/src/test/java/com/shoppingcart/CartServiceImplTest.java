
package com.shoppingcart;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CartServiceImplTest {

    @Mock
    private UserCartItemRepository userCartItemRepository; // Changed to UserCartItemRepository

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private CartServiceImpl cartService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testAddProductToCart_NewItem() {
        User user = new User();
        user.setEmail("test@test.com");

        Product product = new Product();
        product.setPid("1");

        UserCartItemPK pk = new UserCartItemPK(user.getEmail(), product.getPid());

        when(userCartItemRepository.findById(pk)).thenReturn(Optional.empty());
        when(productRepository.findById("1")).thenReturn(Optional.of(product));

        cartService.addProductToCart(user, "1", 1);

        verify(userCartItemRepository, times(1)).save(any(UserCartItem.class));
    }

    @Test
    void testAddProductToCart_ExistingItem() {
        User user = new User();
        user.setEmail("test@test.com");

        Product product = new Product();
        product.setPid("1");

        UserCartItemPK pk = new UserCartItemPK(user.getEmail(), product.getPid());
        UserCartItem existingItem = new UserCartItem(user.getEmail(), product.getPid(), 1);

        when(userCartItemRepository.findById(pk)).thenReturn(Optional.of(existingItem));
        when(productRepository.findById("1")).thenReturn(Optional.of(product));

        cartService.addProductToCart(user, "1", 1);

        assertEquals(2, existingItem.getQuantity());
        verify(userCartItemRepository, times(1)).save(existingItem);
    }

    @Test
    void testRemoveProductFromCart() {
        User user = new User();
        user.setEmail("test@test.com");

        Product product = new Product();
        product.setPid("1");

        UserCartItemPK pk = new UserCartItemPK(user.getEmail(), product.getPid());
        UserCartItem existingItem = new UserCartItem(user.getEmail(), product.getPid(), 1);

        when(userCartItemRepository.findById(pk)).thenReturn(Optional.of(existingItem));

        cartService.removeProductFromCart(user, "1");

        verify(userCartItemRepository, times(1)).deleteById(pk);
    }

    @Test
    void testUpdateProductQuantity() {
        User user = new User();
        user.setEmail("test@test.com");

        Product product = new Product();
        product.setPid("1");

        UserCartItemPK pk = new UserCartItemPK(user.getEmail(), product.getPid());
        UserCartItem existingItem = new UserCartItem(user.getEmail(), product.getPid(), 1);

        when(userCartItemRepository.findById(pk)).thenReturn(Optional.of(existingItem));

        cartService.updateProductQuantity(user, "1", 5);

        assertEquals(5, existingItem.getQuantity());
        verify(userCartItemRepository, times(1)).save(existingItem);
    }

    @Test
    void testClearCart() {
        User user = new User();
        user.setEmail("test@test.com");

        List<UserCartItem> userCartItems = new ArrayList<>();
        userCartItems.add(new UserCartItem(user.getEmail(), "prod1", 1));
        userCartItems.add(new UserCartItem(user.getEmail(), "prod2", 2));

        when(userCartItemRepository.findByUsername(user.getEmail())).thenReturn(userCartItems);

        cartService.clearCart(user);

        verify(userCartItemRepository, times(1)).deleteAll(userCartItems);
    }

    @Test
    void testGetAllCartItems() {
        User user = new User();
        user.setEmail("test@test.com");

        List<UserCartItem> userCartItems = new ArrayList<>();
        userCartItems.add(new UserCartItem(user.getEmail(), "prod1", 1));
        userCartItems.add(new UserCartItem(user.getEmail(), "prod2", 2));

        when(userCartItemRepository.findByUsername(user.getEmail())).thenReturn(userCartItems);

        List<UserCartItem> result = cartService.getAllCartItems(user);

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(userCartItems, result);
    }
}
