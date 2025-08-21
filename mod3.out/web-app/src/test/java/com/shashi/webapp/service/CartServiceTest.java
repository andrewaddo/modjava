package com.shashi.webapp.service;

import com.shashi.dataaccess.entity.Product;
import com.shashi.dataaccess.entity.Usercart;
import com.shashi.dataaccess.entity.UsercartId;
import com.shashi.dataaccess.repository.ProductRepository;
import com.shashi.dataaccess.repository.UsercartRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class CartServiceTest {

    @Mock
    private UsercartRepository usercartRepository;

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private CartService cartService;

    private final String TEST_USERNAME = "test@example.com";
    private final String TEST_PRODID = "P001";
    private final String TEST_PRODID_2 = "P002";

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testAddProductToCart_NewItem() {
        UsercartId id = new UsercartId(TEST_USERNAME, TEST_PRODID);
        when(usercartRepository.findById(id)).thenReturn(Optional.empty());

        cartService.addProductToCart(TEST_USERNAME, TEST_PRODID, 1);

        verify(usercartRepository, times(1)).save(new Usercart(id, 1));
    }

    @Test
    void testAddProductToCart_ExistingItem() {
        UsercartId id = new UsercartId(TEST_USERNAME, TEST_PRODID);
        Usercart existingItem = new Usercart(id, 2);
        when(usercartRepository.findById(id)).thenReturn(Optional.of(existingItem));

        cartService.addProductToCart(TEST_USERNAME, TEST_PRODID, 1);

        verify(usercartRepository, times(1)).save(new Usercart(id, 3));
    }

    @Test
    void testUpdateProductQuantity_UpdateExisting() {
        UsercartId id = new UsercartId(TEST_USERNAME, TEST_PRODID);
        Usercart existingItem = new Usercart(id, 2);
        when(usercartRepository.findById(id)).thenReturn(Optional.of(existingItem));

        cartService.updateProductQuantity(TEST_USERNAME, TEST_PRODID, 5);

        verify(usercartRepository, times(1)).save(new Usercart(id, 5));
    }

    @Test
    void testUpdateProductQuantity_RemoveItem() {
        UsercartId id = new UsercartId(TEST_USERNAME, TEST_PRODID);
        Usercart existingItem = new Usercart(id, 2);
        when(usercartRepository.findById(id)).thenReturn(Optional.of(existingItem));

        cartService.updateProductQuantity(TEST_USERNAME, TEST_PRODID, 0);

        verify(usercartRepository, times(1)).delete(existingItem);
    }

    @Test
    void testUpdateProductQuantity_ItemNotFound() {
        UsercartId id = new UsercartId(TEST_USERNAME, TEST_PRODID);
        when(usercartRepository.findById(id)).thenReturn(Optional.empty());

        cartService.updateProductQuantity(TEST_USERNAME, TEST_PRODID, 5);

        verify(usercartRepository, never()).save(any(Usercart.class));
        verify(usercartRepository, never()).delete(any(Usercart.class));
    }

    @Test
    void testRemoveProductFromCart() {
        doNothing().when(usercartRepository).deleteById_UsernameAndId_Prodid(TEST_USERNAME, TEST_PRODID);

        cartService.removeProductFromCart(TEST_USERNAME, TEST_PRODID);

        verify(usercartRepository, times(1)).deleteById_UsernameAndId_Prodid(TEST_USERNAME, TEST_PRODID);
    }

    @Test
    void testGetCartItemsByUsername() {
        Usercart cartItem1 = new Usercart(new UsercartId(TEST_USERNAME, TEST_PRODID), 1);
        Usercart cartItem2 = new Usercart(new UsercartId(TEST_USERNAME, TEST_PRODID_2), 2);
        List<Usercart> cartItems = Arrays.asList(cartItem1, cartItem2);
        when(usercartRepository.findById_Username(TEST_USERNAME)).thenReturn(cartItems);

        List<Usercart> result = cartService.getCartItemsByUsername(TEST_USERNAME);

        assertNotNull(result);
        assertEquals(2, result.size());
        verify(usercartRepository, times(1)).findById_Username(TEST_USERNAME);
    }

    @Test
    void testGetCartTotal() {
        Usercart cartItem1 = new Usercart(new UsercartId(TEST_USERNAME, TEST_PRODID), 2);
        Usercart cartItem2 = new Usercart(new UsercartId(TEST_USERNAME, TEST_PRODID_2), 1);
        List<Usercart> cartItems = Arrays.asList(cartItem1, cartItem2);

        Product product1 = new Product(TEST_PRODID, "Product 1", "Type", "Info", BigDecimal.valueOf(100.00), 10, null);
        Product product2 = new Product(TEST_PRODID_2, "Product 2", "Type", "Info", BigDecimal.valueOf(50.00), 5, null);

        when(usercartRepository.findById_Username(TEST_USERNAME)).thenReturn(cartItems);
        when(productRepository.findById(TEST_PRODID)).thenReturn(Optional.of(product1));
        when(productRepository.findById(TEST_PRODID_2)).thenReturn(Optional.of(product2));

        BigDecimal total = cartService.getCartTotal(TEST_USERNAME);

        assertEquals(BigDecimal.valueOf(250.00), total);
        verify(usercartRepository, times(1)).findById_Username(TEST_USERNAME);
        verify(productRepository, times(1)).findById(TEST_PRODID);
        verify(productRepository, times(1)).findById(TEST_PRODID_2);
    }

    @Test
    void testGetProductsInCart() {
        Usercart cartItem1 = new Usercart(new UsercartId(TEST_USERNAME, TEST_PRODID), 1);
        List<Usercart> cartItems = Arrays.asList(cartItem1);

        Product product1 = new Product(TEST_PRODID, "Product 1", "Type", "Info", BigDecimal.valueOf(100.00), 10, null);

        when(usercartRepository.findById_Username(TEST_USERNAME)).thenReturn(cartItems);
        when(productRepository.findById(TEST_PRODID)).thenReturn(Optional.of(product1));

        List<Product> productsInCart = cartService.getProductsInCart(TEST_USERNAME);

        assertNotNull(productsInCart);
        assertEquals(1, productsInCart.size());
        assertEquals(TEST_PRODID, productsInCart.get(0).getPid());
        verify(usercartRepository, times(1)).findById_Username(TEST_USERNAME);
        verify(productRepository, times(1)).findById(TEST_PRODID);
    }
}
