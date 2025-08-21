package com.shashi.webapp.service;

import org.springframework.stereotype.Service;

@Service
public class EmailService {

    public void sendRegistrationConfirmation(String recipientEmail, String username) {
        System.out.println("Sending registration confirmation to: " + recipientEmail);
        System.out.println("Subject: Welcome to Shopping Cart!");
        System.out.println("Body: Dear " + username + ",\n\nWelcome to our e-commerce platform! Your registration was successful.\n\nThank you,\nThe Shopping Cart Team");
    }

    public void sendOrderConfirmation(String recipientEmail, String orderId, String username) {
        System.out.println("Sending order confirmation to: " + recipientEmail);
        System.out.println("Subject: Your Order " + orderId + " Confirmation");
        System.out.println("Body: Dear " + username + ",\n\nThank you for your order! Your order ID is: " + orderId + ".\n\nThank you,\nThe Shopping Cart Team");
    }

    public void sendShippingUpdate(String recipientEmail, String orderId, String prodid, String username) {
        System.out.println("Sending shipping update to: " + recipientEmail);
        System.out.println("Subject: Your Order " + orderId + " has been Shipped!");
        System.out.println("Body: Dear " + username + ",\n\nGood news! The product " + prodid + " from order " + orderId + " has been shipped.\n\nThank you,\nThe Shopping Cart Team");
    }

    public void sendBackInStockNotification(String recipientEmail, String productName, String username) {
        System.out.println("Sending back-in-stock notification to: " + recipientEmail);
        System.out.println("Subject: " + productName + " is Back in Stock!");
        System.out.println("Body: Dear " + username + ",\n\nGood news! The product " + productName + " you were interested in is now back in stock!\n\nThank you,\nThe Shopping Cart Team");
    }
}
