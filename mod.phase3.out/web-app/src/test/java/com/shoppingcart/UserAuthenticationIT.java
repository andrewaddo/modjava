package com.shoppingcart;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;

import java.time.Duration;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class UserAuthenticationIT extends BaseIntegrationTest {

    @LocalServerPort
    private int port;

    @Container
    public static PostgreSQLContainer<?> postgreSQLContainer = new PostgreSQLContainer<>("postgres:13");

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
    }

    @AfterEach
    void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }

    @Test
    void testUserRegistrationAndLogin() {
        // Navigate to registration page
        driver.get("http://localhost:" + port + "/register");
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("email")));

        // Fill registration form
        driver.findElement(By.id("name")).sendKeys("Test User");
        driver.findElement(By.id("email")).sendKeys("test@example.com");
        driver.findElement(By.id("password")).sendKeys("password");
        driver.findElement(By.xpath("//button[text()='Register']")).click();

        // Verify successful registration and redirection to login
        wait.until(ExpectedConditions.urlContains("/login"));
        

        // Fill login form
        driver.findElement(By.id("username")).sendKeys("test@example.com");
        driver.findElement(By.id("password")).sendKeys("password");
        driver.findElement(By.xpath("//button[text()='Login']")).click();

        // Verify successful login and redirection to root URL
        wait.until(ExpectedConditions.urlContains("/"));
        // Verify presence of Logout link, indicating successful authentication
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//button[text()='Logout']")));
    }

    @Test
    void testUserLogout() {
        // First, log in a user
        driver.get("http://localhost:" + port + "/login");
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("username")));
        driver.findElement(By.id("username")).sendKeys("test@example.com");
        driver.findElement(By.id("password")).sendKeys("password");
        driver.findElement(By.xpath("//button[text()='Login']")).click();
        wait.until(ExpectedConditions.urlContains("/"));
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//button[text()='Logout']"))); // Ensure logged in

        // Now, click the Logout link
        driver.findElement(By.xpath("//button[text()='Logout']")).click();

        // Assert successful logout:
        // 1. Should be redirected to the login page or root
        wait.until(ExpectedConditions.urlContains("/")); // Redirect to home page
        // 2. Logout link should become invisible
        wait.until(ExpectedConditions.invisibilityOfElementLocated(By.linkText("Logout")));

        // 3. Login link should be visible again
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.linkText("Login")));
    }
}
