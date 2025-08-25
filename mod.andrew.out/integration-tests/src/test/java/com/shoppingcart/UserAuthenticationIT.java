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

import org.springframework.test.context.ActiveProfiles;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Testcontainers
@ActiveProfiles("test")
public class UserAuthenticationIT {

    @LocalServerPort
    private int port;

    @Container
    public static PostgreSQLContainer<?> postgreSQLContainer = new PostgreSQLContainer<>("postgres:13")
            .withDatabaseName("testdb")
            .withUsername("testuser")
            .withPassword("testpass");

    @Test
    void testUserRegistrationAndLogin() {
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--headless"); // Run in headless mode
        WebDriver driver = new ChromeDriver(options);
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

        try {
            // Navigate to registration page
            driver.get("http://localhost:" + port + "/register");
            wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("email")));

            // Fill registration form
            driver.findElement(By.id("name")).sendKeys("Test User");
            driver.findElement(By.id("email")).sendKeys("test@example.com");
            driver.findElement(By.id("password")).sendKeys("password");
            driver.findElement(By.id("registerButton")).click();

            // Verify successful registration and redirection to login
            wait.until(ExpectedConditions.urlContains("/login"));
            assertTrue(driver.getPageSource().contains("Registration successful. Please login."));

            // Fill login form
            driver.findElement(By.id("email")).sendKeys("test@example.com");
            driver.findElement(By.id("password")).sendKeys("password");
            driver.findElement(By.id("loginButton")).click();

            // Verify successful login and redirection to user home
            wait.until(ExpectedConditions.urlContains("/userHome"));
            assertTrue(driver.getPageSource().contains("Welcome, Test User!"));

        } finally {
            driver.quit();
        }
    }
}