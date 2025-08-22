package com.shashi.webapp.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class EmailServiceTest {

    @InjectMocks
    private EmailService emailService;

    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    private final PrintStream originalOut = System.out;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        System.setOut(new PrintStream(outContent));
    }

    @Test
    void testSendRegistrationConfirmation() {
        String email = "test@example.com";
        String username = "Test User";
        emailService.sendRegistrationConfirmation(email, username);
        assertTrue(outContent.toString().contains("Sending registration confirmation to: " + email));
        assertTrue(outContent.toString().contains("Subject: Welcome to Shopping Cart!"));
        assertTrue(outContent.toString().contains("Body: Dear " + username + ",\n\nWelcome to our e-commerce platform! Your registration was successful."));
    }

    @Test
    void testSendOrderConfirmation() {
        String email = "test@example.com";
        String orderId = "ORDER123";
        String username = "Test User";
        emailService.sendOrderConfirmation(email, orderId, username);
        assertTrue(outContent.toString().contains("Sending order confirmation to: " + email));
        assertTrue(outContent.toString().contains("Subject: Your Order " + orderId + " Confirmation"));
        assertTrue(outContent.toString().contains("Body: Dear " + username + ",\n\nThank you for your order! Your order ID is: " + orderId + "."));
    }

        @Test
    void testSendShippingUpdate() {
        String email = "test@example.com";
        String orderId = "ORDER123";
        String prodid = "PROD001";
        String username = "Test User";
        emailService.sendShippingUpdate(email, orderId, prodid, username);
        String expectedOutput = "Sending shipping update to: " + email + "\n" +
                                "Subject: Your Order " + orderId + " has been Shipped!\n" +
                                "Body: Dear " + username + ",\n\nGood news! The product " + prodid + " from order " + orderId + " has been shipped.\n\nThank you,\nThe Shopping Cart Team\n";
        assertEquals(expectedOutput, outContent.toString());
    }

    @Test
    void testSendBackInStockNotification() {
        String email = "test@example.com";
        String productName = "Test Product";
        String username = "Test User";
        emailService.sendBackInStockNotification(email, productName, username);
        assertTrue(outContent.toString().contains("Sending back-in-stock notification to: " + email));
        assertTrue(outContent.toString().contains("Subject: " + productName + " is Back in Stock!"));
        assertTrue(outContent.toString().contains("Body: Dear " + username + ",\n\nGood news! The product " + productName + " you were interested in is now back in stock!"));
    }
}
