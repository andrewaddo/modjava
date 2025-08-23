package com.shoppingcart;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.support.ui.ExpectedConditions;

import java.time.Duration;

import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Testcontainers
public class ShoppingCartAndOrderProcessingIT {

    @LocalServerPort
    private int port;

    @Container
    public static PostgreSQLContainer<?> postgreSQLContainer = new PostgreSQLContainer<>("postgres:13")
            .withDatabaseName("testdb")
            .withUsername("testuser")
            .withPassword("testpass");

    @Test
    void testShoppingCartAndOrderProcessing() {
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--headless"); // Run in headless mode
        WebDriver driver = new ChromeDriver(options);
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

        try {
            // User Login (assuming a user is already registered)
            driver.get("http://localhost:" + port + "/login");
            wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("email")));
            driver.findElement(By.id("email")).sendKeys("user@test.com");
            driver.findElement(By.id("password")).sendKeys("password");
            driver.findElement(By.id("loginButton")).click();
            wait.until(ExpectedConditions.urlContains("/userHome"));
            assertTrue(driver.getPageSource().contains("Welcome, user@test.com!"));

            // Add product to cart (assuming a product exists)
            driver.get("http://localhost:" + port + "/product?id=1"); // Assuming product with ID 1 exists
            wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("addToCartButton")));
            driver.findElement(By.id("addToCartButton")).click();
            wait.until(ExpectedConditions.urlContains("/cart"));
            assertTrue(driver.getPageSource().contains("Product added to cart"));

            // Update cart quantity
            driver.findElement(By.id("quantity_1")).clear(); // Assuming input field for product 1 quantity
            driver.findElement(By.id("quantity_1")).sendKeys("3");
            driver.findElement(By.id("updateCartButton")).click();
            wait.until(ExpectedConditions.urlContains("/cart"));
            assertTrue(driver.getPageSource().contains("Quantity updated"));

            // Remove product from cart
            driver.findElement(By.id("remove_1")).click(); // Assuming remove button for product 1
            wait.until(ExpectedConditions.urlContains("/cart"));
            assertTrue(driver.getPageSource().contains("Product removed from cart"));

            // Add product to cart again for checkout
            driver.get("http://localhost:" + port + "/product?id=1");
            wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("addToCartButton")));
            driver.findElement(By.id("addToCartButton")).click();
            wait.until(ExpectedConditions.urlContains("/cart"));

            // Proceed to checkout
            driver.findElement(By.id("checkoutButton")).click();
            wait.until(ExpectedConditions.urlContains("/checkout"));
            assertTrue(driver.getPageSource().contains("Checkout"));

            // Place order
            driver.findElement(By.id("placeOrderButton")).click();
            wait.until(ExpectedConditions.urlContains("/orderConfirmation"));
            assertTrue(driver.getPageSource().contains("Order placed successfully"));

        } finally {
            driver.quit();
        }
    }
}