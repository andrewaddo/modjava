package com.shoppingcart;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.support.ui.ExpectedConditions;


import java.time.Duration;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@Testcontainers
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = ShoppingCartApplication.class)
class ShoppingCartIT {

    @LocalServerPort
    private int port;

    @Container
    public static PostgreSQLContainer<?> postgreSQLContainer = new PostgreSQLContainer<>("postgres:13");

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ProductRepository productRepository;

    private WebDriver driver;
    private WebDriverWait wait;

    @DynamicPropertySource
    static void databaseProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgreSQLContainer::getJdbcUrl);
        registry.add("spring.datasource.username", postgreSQLContainer::getUsername);
        registry.add("spring.datasource.password", postgreSQLContainer::getPassword);
    }

    @BeforeEach
    void setUp() {
        WebDriverManager.chromedriver().setup();
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--no-sandbox");
        options.addArguments("--headless");
        options.addArguments("--disable-dev-shm-usage");
        driver = new ChromeDriver(options);
        wait = new WebDriverWait(driver, Duration.ofSeconds(20));

        // Create a user
        User user = new User();
        user.setEmail("user@test.com");
        user.setPassword("password");
        user.setName("Test User");
        user.setRole("USER");
        user.setAddress("123 Test St");
        user.setPincode(12345);
        userRepository.save(user);

        // Create a product
        Product product = new Product();
        product.setPid("P001");
        product.setName("Test Camera");
        product.setInfo("A test camera product.");
        product.setPrice(199.99);
        product.setQuantity(10);
        productRepository.save(product);
    }

    @AfterEach
    void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }

    @Test
    void testAddToCartAndCheckout() {
        // Login
        driver.get("http://localhost:" + port + "/login");
        driver.findElement(By.id("username")).sendKeys("user@test.com");
        driver.findElement(By.id("password")).sendKeys("password");
        driver.findElement(By.cssSelector("button[type='submit']")).click();

        // Wait for the "Add to Cart" button to be visible, which indicates login is complete and products are displayed.
        WebElement addToCartButton = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//form[@action='/cart/add/P001']/button")));

        // Add product to cart
        addToCartButton.click();
        
        // Go to cart
        driver.get("http://localhost:" + port + "/cart");

        // Verify product is in cart
        WebElement productName = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//td[text()='Test Camera']")));
        assertEquals("Test Camera", productName.getText());

        // Proceed to checkout
        driver.get("http://localhost:" + port + "/checkout");

        // Wait for the payment page to load
        wait.until(ExpectedConditions.urlContains("/checkout"));
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//h2[text()='Credit Card Payment']")));

        // On payment page, click "Pay Now"
        wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//button[contains(text(),'Pay :Rs')]"))).click();

        // Verify order is placed and cart is empty
        wait.until(ExpectedConditions.urlContains("/orders"));
        driver.get("http://localhost:" + port + "/cart");
        
        WebElement emptyCartMessage = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//h3[text()='Your cart is empty!']")));
        assertTrue(emptyCartMessage.isDisplayed());
    }
}
