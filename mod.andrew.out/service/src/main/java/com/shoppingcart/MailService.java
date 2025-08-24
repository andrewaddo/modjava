package com.shoppingcart;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

@Service
public class MailService {

    @Autowired
    private JavaMailSender mailSender;

    public void sendRegistrationSuccessEmail(String recipientEmail, String name) throws MessagingException {
        MimeMessage mimeMessage = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, "utf-8");
        String htmlMsg = "<h3>Welcome, " + name + "!</h3><p>Thank you for registering with Ellison Electronics.</p>";
        helper.setText(htmlMsg, true);
        helper.setTo(recipientEmail);
        helper.setSubject("Registration Successful - Ellison Electronics");
        helper.setFrom("noreply@ellison.com"); // Replace with your sender email
        mailSender.send(mimeMessage);
    }

    public void sendTransactionSuccessEmail(String recipientEmail, String name, String transactionId, double amount) throws MessagingException {
        MimeMessage mimeMessage = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, "utf-8");
        String htmlMsg = "<h3>Order Placed Successfully!</h3><p>Dear " + name + ",</p>" +
                         "<p>Your order with Transaction ID: <strong>" + transactionId + "</strong> has been placed successfully.</p>" +
                         "<p>Total Amount: <strong>Rs " + String.format("%.2f", amount) + "</strong></p>" +
                         "<p>Thank you for shopping with Ellison Electronics.</p>";
        helper.setText(htmlMsg, true);
        helper.setTo(recipientEmail);
        helper.setSubject("Order Confirmation - Ellison Electronics");
        helper.setFrom("noreply@ellison.com"); // Replace with your sender email
        mailSender.send(mimeMessage);
    }

    public void sendOrderShippedEmail(String recipientEmail, String name, String orderId, double amount) throws MessagingException {
        MimeMessage mimeMessage = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, "utf-8");
        String htmlMsg = "<h3>Your Order Has Been Shipped!</h3><p>Dear " + name + ",</p>" +
                         "<p>Your order with Order ID: <strong>" + orderId + "</strong> has been shipped.</p>" +
                         "<p>Amount: <strong>Rs " + String.format("%.2f", amount) + "</strong></p>" +
                         "<p>Thank you for shopping with Ellison Electronics.</p>";
        helper.setText(htmlMsg, true);
        helper.setTo(recipientEmail);
        helper.setSubject("Order Shipped - Ellison Electronics");
        helper.setFrom("noreply@ellison.com"); // Replace with your sender email
        mailSender.send(mimeMessage);
    }

    public void sendProductAvailableEmail(String recipientEmail, String name, String productName, String productId) throws MessagingException {
        MimeMessage mimeMessage = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, "utf-8");
        String htmlMsg = "<h3>Product Back in Stock!</h3><p>Dear " + name + ",</p>" +
                         "<p>The product <strong>" + productName + "</strong> (ID: " + productId + ") you were waiting for is now back in stock!</p>" +
                         "<p>Visit our store to purchase it now.</p>" +
                         "<p>Thank you for your patience.</p>";
        helper.setText(htmlMsg, true);
        helper.setTo(recipientEmail);
        helper.setSubject("Product Back in Stock - Ellison Electronics");
        helper.setFrom("noreply@ellison.com"); // Replace with your sender email
        mailSender.send(mimeMessage);
    }

    public void sendContactMessage(String toEmail, String subject, String messageContent) throws MessagingException {
        MimeMessage mimeMessage = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, "utf-8");
        helper.setText(messageContent, false); // Not HTML
        helper.setTo(toEmail);
        helper.setSubject(subject);
        helper.setFrom("noreply@ellison.com"); // Replace with your sender email
        mailSender.send(mimeMessage);
    }
}
