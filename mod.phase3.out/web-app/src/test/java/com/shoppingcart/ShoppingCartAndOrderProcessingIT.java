package com.shoppingcart;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.BrowserWebDriverContainer;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.time.Duration;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;

public class ShoppingCartAndOrderProcessingIT extends BaseIntegrationTest {

    @LocalServerPort
    private int port;

    @Container
    public static PostgreSQLContainer<?> postgreSQLContainer = new PostgreSQLContainer<>("postgres:13");

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    private WebDriverWait wait;

    @DynamicPropertySource
    static void databaseProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgreSQLContainer::getJdbcUrl);
        registry.add("spring.datasource.username", postgreSQLContainer::getUsername);
        registry.add("spring.datasource.password", postgreSQLContainer::getPassword);
    }

    @BeforeEach
    void setUp() {
        wait = new WebDriverWait(driver, Duration.ofSeconds(20));

        // Setup test user and product (similar to ShoppingCartIT)
        User user = new User();
        user.setEmail("user@test.com");
        user.setPassword(passwordEncoder.encode("password"));
        user.setName("Test User");
        user.setRole("USER");
        user.setAddress("123 Test St");
        user.setPincode(12345);
        userRepository.save(user);

        Product product = new Product();
        product.setPid("P001");
        product.setName("Test Camera");
        product.setInfo("A test camera product.");
        product.setPrice(199.99);
        product.setQuantity(10);
        productRepository.save(product);
    }

    

    @Test
    void testShoppingCartAndOrderProcessing() {
        // User Login
        driver.get("http://host.testcontainers.internal:" + port + "/login");
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("username")));
        driver.findElement(By.id("username")).sendKeys("user@test.com");
        driver.findElement(By.id("password")).sendKeys("password");
        driver.findElement(By.xpath("//button[text()='Login']")).click();
        wait.until(ExpectedConditions.urlContains("/"));
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//button[text()='Logout']"))); // Ensure logged in

        // Add product to cart
        driver.get("http://host.testcontainers.internal:" + port + "/product-details/P001");
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//button[text()='Add to Cart']")));
        driver.findElement(By.xpath("//button[text()='Add to Cart']")).click();
        wait.until(ExpectedConditions.urlContains("/cart"));
        // Assert product is in cart by checking its name
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//td[text()='Test Camera']")));

        // Removed "Update cart quantity" section as UI does not support it

        // Remove product from cart
        // Navigate to cart page explicitly if not already there
        driver.get("http://host.testcontainers.internal:" + port + "/cart");
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//a[text()='Remove']")));
        driver.findElement(By.xpath("//a[text()='Remove']")).click();
        wait.until(ExpectedConditions.urlContains("/cart"));
        // Assert product is removed from cart
        wait.until(ExpectedConditions.invisibilityOfElementLocated(By.xpath("//td[text()='Test Camera']")));
        assertTrue(driver.getPageSource().contains("Your cart is empty!"));

        // Add product to cart again for checkout
        driver.get("http://host.testcontainers.internal:" + port + "/product-details/P001");
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//button[text()='Add to Cart']")));
        driver.findElement(By.xpath("//button[text()='Add to Cart']")).click();
        wait.until(ExpectedConditions.urlContains("/cart"));

        // Proceed to checkout
        driver.get("http://host.testcontainers.internal:" + port + "/cart");
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//a[text()='Checkout']")));
        driver.findElement(By.xpath("//a[text()='Checkout']")).click();
        wait.until(ExpectedConditions.urlContains("/checkout"));
        assertTrue(driver.getPageSource().contains("Credit Card Payment"));

        // Place order
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//button[contains(text(),'Pay')]")));
        driver.findElement(By.xpath("//button[contains(text(),'Pay')]")).click();
        wait.until(ExpectedConditions.urlContains("/orders"));
        // Assert order is placed by checking for transaction table or a success message on orders page
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//h1[text()='My Transactions']")));
    }
}