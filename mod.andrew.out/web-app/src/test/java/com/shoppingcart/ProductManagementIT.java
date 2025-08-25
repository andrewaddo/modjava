
package com.shoppingcart;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Disabled;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;

import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.support.ui.ExpectedConditions;
import java.time.Duration;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.springframework.test.context.ActiveProfiles;

@Testcontainers
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = ShoppingCartApplication.class)
@ActiveProfiles("test")
@Disabled
class ProductManagementIT {

    @LocalServerPort
    private int port;

    @Container
    public static PostgreSQLContainer<?> postgreSQLContainer = new PostgreSQLContainer<>("postgres:13");

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    private WebDriver driver;

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

        User admin = new User();
        admin.setEmail("admin@test.com");
        admin.setPassword(passwordEncoder.encode("password"));
        admin.setName("Admin");
        admin.setRole("ADMIN");
        admin.setAddress("Admin Address");
        admin.setPincode(123456);
        userRepository.save(admin);
    }

    @AfterEach
    void tearDown() {
        driver.quit();
    }

    @Test
    void testProductManagement() {
        driver.get("http://localhost:" + port + "/admin/products");
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(20));

        wait.until(ExpectedConditions.urlContains("/login"));

        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("username"))).sendKeys("admin@test.com");
        driver.findElement(By.id("password")).sendKeys("password");
        driver.findElement(By.cssSelector("button[type='submit']")).click();

        wait.until(ExpectedConditions.urlContains("/admin/products"));

        wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//a[text()='Add Product']"))).click();

        wait.until(ExpectedConditions.urlContains("/admin/products/add"));

        WebElement nameElement = wait.until(ExpectedConditions.presenceOfElementLocated(By.id("name")));
        nameElement.sendKeys("Test Product");

        WebElement infoElement = driver.findElement(By.id("info"));
        infoElement.sendKeys("Test Info");

        WebElement priceElement = driver.findElement(By.id("price"));
        priceElement.sendKeys("10.0");

        WebElement quantityElement = driver.findElement(By.id("quantity"));
        quantityElement.sendKeys("10");

        WebElement imageElement = driver.findElement(By.id("image"));
        imageElement.sendKeys("https://example.com/image.jpg");

        

        driver.findElement(By.cssSelector("button[type='submit']")).click();

        wait.until(ExpectedConditions.urlContains("/admin/products"));

        WebElement productLink = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//a[text()='Test Product']")));
        assertEquals("Test Product", productLink.getText());
    }
}
