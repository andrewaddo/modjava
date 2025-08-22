package com.shashi.service.cart;

import com.shashi.dataaccess.entity.UserCartEntity;
import com.shashi.dataaccess.repository.UserCartRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class CartServiceImplTest {

    @Mock
    private UserCartRepository userCartRepository;

    @InjectMocks
    private CartServiceImpl cartService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testAddProductToCart_newProduct() {
        String username = "test@example.com";
        String prodid = "P001";
        Integer quantity = 2;

        when(userCartRepository.findByUsernameAndProdid(username, prodid)).thenReturn(null);
        when(userCartRepository.save(any(UserCartEntity.class))).thenAnswer(invocation -> invocation.getArgument(0));

        UserCartEntity result = cartService.addProductToCart(username, prodid, quantity);

        assertNotNull(result);
        assertEquals(username, result.getUsername());
        assertEquals(prodid, result.getProdid());
        assertEquals(quantity, result.getQuantity());
        verify(userCartRepository, times(1)).findByUsernameAndProdid(username, prodid);
        verify(userCartRepository, times(1)).save(any(UserCartEntity.class));
    }

    @Test
    void testAddProductToCart_existingProduct() {
        String username = "test@example.com";
        String prodid = "P001";
        Integer existingQuantity = 3;
        Integer newQuantity = 2;

        UserCartEntity existingCartItem = new UserCartEntity();
        existingCartItem.setUsername(username);
        existingCartItem.setProdid(prodid);
        existingCartItem.setQuantity(existingQuantity);

        when(userCartRepository.findByUsernameAndProdid(username, prodid)).thenReturn(existingCartItem);
        when(userCartRepository.save(any(UserCartEntity.class))).thenAnswer(invocation -> invocation.getArgument(0));

        UserCartEntity result = cartService.addProductToCart(username, prodid, newQuantity);

        assertNotNull(result);
        assertEquals(username, result.getUsername());
        assertEquals(prodid, result.getProdid());
        assertEquals(existingQuantity + newQuantity, result.getQuantity());
        verify(userCartRepository, times(1)).findByUsernameAndProdid(username, prodid);
        verify(userCartRepository, times(1)).save(existingCartItem);
    }

    @Test
    void testUpdateProductQuantityInCart_existingProduct() {
        String username = "test@example.com";
        String prodid = "P001";
        Integer newQuantity = 5;

        UserCartEntity existingCartItem = new UserCartEntity();
        existingCartItem.setUsername(username);
        existingCartItem.setProdid(prodid);
        existingCartItem.setQuantity(2);

        when(userCartRepository.findByUsernameAndProdid(username, prodid)).thenReturn(existingCartItem);
        when(userCartRepository.save(any(UserCartEntity.class))).thenAnswer(invocation -> invocation.getArgument(0));

        UserCartEntity result = cartService.updateProductQuantityInCart(username, prodid, newQuantity);

        assertNotNull(result);
        assertEquals(newQuantity, result.getQuantity());
        verify(userCartRepository, times(1)).findByUsernameAndProdid(username, prodid);
        verify(userCartRepository, times(1)).save(existingCartItem);
    }

    @Test
    void testUpdateProductQuantityInCart_productNotFound() {
        String username = "test@example.com";
        String prodid = "P001";
        Integer newQuantity = 5;

        when(userCartRepository.findByUsernameAndProdid(username, prodid)).thenReturn(null);

        UserCartEntity result = cartService.updateProductQuantityInCart(username, prodid, newQuantity);

        assertNull(result);
        verify(userCartRepository, times(1)).findByUsernameAndProdid(username, prodid);
        verify(userCartRepository, never()).save(any(UserCartEntity.class));
    }

    @Test
    void testRemoveProductFromCart() {
        String username = "test@example.com";
        String prodid = "P001";

        doNothing().when(userCartRepository).deleteByUsernameAndProdid(username, prodid);

        cartService.removeProductFromCart(username, prodid);

        verify(userCartRepository, times(1)).deleteByUsernameAndProdid(username, prodid);
    }

    @Test
    void testGetCartItemsByUsername() {
        String username = "test@example.com";
        UserCartEntity item1 = new UserCartEntity();
        item1.setProdid("P001");
        UserCartEntity item2 = new UserCartEntity();
        item2.setProdid("P002");
        List<UserCartEntity> cartItems = Arrays.asList(item1, item2);

        when(userCartRepository.findByUsername(username)).thenReturn(cartItems);

        List<UserCartEntity> result = cartService.getCartItemsByUsername(username);

        assertNotNull(result);
        assertEquals(2, result.size());
        verify(userCartRepository, times(1)).findByUsername(username);
    }

    @Test
    void testClearCart() {
        String username = "test@example.com";

        doNothing().when(userCartRepository).deleteByUsername(username);

        cartService.clearCart(username);

        verify(userCartRepository, times(1)).deleteByUsername(username);
    }
}
