package com.shashi.integration.tests;

import com.shashi.webapp.ShoppingCartApplication;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;

@Testcontainers
@SpringBootTest(classes = ShoppingCartApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class UserAuthIT {

    @LocalServerPort
    private int port;

    @Container
    public static PostgreSQLContainer<?> postgreSQLContainer = new PostgreSQLContainer<>("postgres:13")
            .withDatabaseName("testdb")
            .withUsername("testuser")
            .withPassword("testpass");

    private WebDriver driver;

    @DynamicPropertySource
    static void registerPgProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgreSQLContainer::getJdbcUrl);
        registry.add("spring.datasource.username", postgreSQLContainer::getUsername);
        registry.add("spring.datasource.password", postgreSQLContainer::getPassword);
        registry.add("spring.jpa.hibernate.ddl-auto", () -> "create-drop"); // Create schema for tests
    }

    @BeforeEach
    void setUp() {
        // Setup ChromeOptions for headless execution in CI/CD environments
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--headless");
        options.addArguments("--disable-gpu");
        options.addArguments("--no-sandbox");
        options.addArguments("--disable-dev-shm-usage");
        driver = new ChromeDriver(options);
    }

    @AfterEach
    void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }

    @Test
    void testUserRegistrationAndLogin() {
        String baseUrl = "http://localhost:" + port;
        String testEmail = "testuser@example.com";
        String testPassword = "password123";
        String testName = "Test User";
        String testMobile = "1234567890";
        String testAddress = "123 Test St";
        String testPincode = "123456";

        // 1. Test Registration
        driver.get(baseUrl + "/register.jsp");
        driver.findElement(By.name("email")).sendKeys(testEmail);
        driver.findElement(By.name("name")).sendKeys(testName);
        driver.findElement(By.name("mobile")).sendKeys(testMobile);
        driver.findElement(By.name("address")).sendKeys(testAddress);
        driver.findElement(By.name("pincode")).sendKeys(testPincode);
        driver.findElement(By.name("password")).sendKeys(testPassword);
        driver.findElement(By.name("cpassword")).sendKeys(testPassword);
        driver.findElement(By.cssSelector("input[type='submit']")).click();

        // Verify successful registration (e.g., redirected to login page or user home)
        // Assuming successful registration redirects to login.jsp or shows a success message
        assertTrue(driver.getCurrentUrl().contains("login.jsp") || driver.getPageSource().contains("Registration successful"),
                "Registration was not successful or did not redirect correctly.");

        // 2. Test Login
        driver.get(baseUrl + "/login.jsp"); // Ensure we are on the login page
        driver.findElement(By.name("email")).sendKeys(testEmail);
        driver.findElement(By.name("password")).sendKeys(testPassword);
        driver.findElement(By.cssSelector("input[type='submit']")).click();

        // Verify successful login (e.g., redirected to userHome.jsp or shows welcome message)
        assertTrue(driver.getCurrentUrl().contains("userHome.jsp") || driver.getPageSource().contains("Welcome, " + testName),
                "Login was not successful or did not redirect correctly.");

        // Test invalid login (optional, but good to have)
        driver.get(baseUrl + "/login.jsp");
        driver.findElement(By.name("email")).sendKeys("wrong@example.com");
        driver.findElement(By.name("password")).sendKeys("wrongpass");
        driver.findElement(By.cssSelector("input[type='submit']")).click();

        assertFalse(driver.getCurrentUrl().contains("userHome.jsp"), "Invalid login should not redirect to user home.");
        assertTrue(driver.getPageSource().contains("Invalid Email or Password"), "Invalid login should show error message.");
    }
}
