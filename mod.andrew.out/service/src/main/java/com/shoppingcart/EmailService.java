package com.shoppingcart;

import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

@Service
public class EmailService {

    private final JavaMailSender mailSender;
    private final TemplateEngine templateEngine;

    public EmailService(JavaMailSender mailSender, TemplateEngine templateEngine) {
        this.mailSender = mailSender;
        this.templateEngine = templateEngine;
    }

    public void sendRegistrationSuccessEmail(String to, String name) throws MessagingException {
        Context context = new Context();
        context.setVariable("name", name);
        String htmlContent = templateEngine.process("registration-success", context);

        sendHtmlEmail(to, "Welcome to Ellison Electronics", htmlContent);
    }

    public void sendTransactionSuccessEmail(String to, String name, String transactionId, double amount) throws MessagingException {
        Context context = new Context();
        context.setVariable("name", name);
        context.setVariable("transactionId", transactionId);
        context.setVariable("amount", amount);
        String htmlContent = templateEngine.process("transaction-success", context);

        sendHtmlEmail(to, "Order Placed at Ellison Electronics", htmlContent);
    }

    public void sendOrderShippedEmail(String to, String name, String transactionId, double amount) throws MessagingException {
        Context context = new Context();
        context.setVariable("name", name);
        context.setVariable("transactionId", transactionId);
        context.setVariable("amount", amount);
        String htmlContent = templateEngine.process("order-shipped", context);

        sendHtmlEmail(to, "Hurray!!, Your Order has been Shipped from Ellison Electronics", htmlContent);
    }

    public void sendProductAvailableEmail(String to, String name, String productName, String productId) throws MessagingException {
        Context context = new Context();
        context.setVariable("name", name);
        context.setVariable("productName", productName);
        context.setVariable("productId", productId);
        String htmlContent = templateEngine.process("product-available", context);

        sendHtmlEmail(to, "Product " + productName + " is Now Available at Ellison Electronics", htmlContent);
    }

    public void sendContactMessage(String toEmail, String subject, String messageContent) throws MessagingException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, "utf-8");
        helper.setText(messageContent, false); // Not HTML
        helper.setTo(toEmail);
        helper.setSubject(subject);
        helper.setFrom("noreply@ellison.com"); // Replace with your sender email
        mailSender.send(message);
    }

    private void sendHtmlEmail(String to, String subject, String htmlContent) throws MessagingException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
        helper.setTo(to);
        helper.setSubject(subject);
        helper.setText(htmlContent, true);
        mailSender.send(message);
    }
}