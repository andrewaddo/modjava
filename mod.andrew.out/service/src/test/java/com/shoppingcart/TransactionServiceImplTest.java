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

import jakarta.mail.MessagingException;

class TransactionServiceImplTest { // Renamed class

    @Mock
    private TransactionRepository transactionRepository; // Mock TransactionRepository

    @Mock
    private OrderRepository orderRepository; // Mock OrderRepository

    @Mock
    private ProductRepository productRepository;

    @Mock
    private EmailService emailService;

    @InjectMocks
    private TransactionServiceImpl transactionService; // Inject TransactionServiceImpl

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreateTransaction() throws MessagingException { // Renamed method
        User user = new User();
        user.setEmail("test@test.com");
        user.setName("Test User");

        Product product = new Product();
        product.setPid("P001");
        product.setPrice(10.0);

        UserCartItem userCartItem = new UserCartItem(user.getEmail(), product.getPid(), 2);

        List<UserCartItem> userCartItems = new ArrayList<>();
        userCartItems.add(userCartItem);

        when(productRepository.findById(product.getPid())).thenReturn(Optional.of(product));
        when(transactionRepository.save(any(Transaction.class))).thenAnswer(i -> i.getArguments()[0]);
        

        doNothing().when(emailService).sendTransactionSuccessEmail(anyString(), anyString(), anyString(), anyDouble());

        Transaction transaction = transactionService.createTransaction(user, userCartItems); // Call createTransaction

        assertNotNull(transaction);
        assertEquals(1, transaction.getOrders().size()); // Check orders list
        assertEquals(20.0, transaction.getAmount()); // Check total amount
        // Removed status check from Transaction
        verify(transactionRepository, times(1)).save(any(Transaction.class));
        
        verify(emailService, times(1)).sendTransactionSuccessEmail(user.getEmail(), user.getName(), transaction.getTransid(), transaction.getAmount());
    }

    @Test
    void testGetTransactionById() { // Renamed method
        Transaction transaction = new Transaction();
        transaction.setTransid("TR123");

        when(transactionRepository.findById("TR123")).thenReturn(Optional.of(transaction));

        Transaction result = transactionService.getTransactionById("TR123"); // Call getTransactionById

        assertNotNull(result);
        assertEquals("TR123", result.getTransid());
    }

    @Test
    void testGetTransactionsByUser() { // Renamed method
        User user = new User();
        user.setEmail("test@test.com");

        Transaction transaction1 = new Transaction();
        transaction1.setTransid("TR001");
        transaction1.setUser(user);

        Transaction transaction2 = new Transaction();
        transaction2.setTransid("TR002");
        transaction2.setUser(user);

        when(transactionRepository.findByUser(user)).thenReturn(Arrays.asList(transaction1, transaction2));

        List<Transaction> transactions = transactionService.getTransactionsByUser(user); // Call getTransactionsByUser

        assertEquals(2, transactions.size());
        verify(transactionRepository, times(1)).findByUser(user);
    }

    // Removed testUpdateOrderStatus as status is no longer on Transaction
}
