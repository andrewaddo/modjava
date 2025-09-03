package com.shoppingcart;

import org.junit.jupiter.api.BeforeEach;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.testcontainers.containers.BrowserWebDriverContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.mockito.Mockito.when;
import static org.mockito.ArgumentMatchers.anyString;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = ShoppingCartApplication.class)
@Testcontainers
@ActiveProfiles("test")
public abstract class BaseIntegrationTest {

    @MockBean
    protected AiProductInfoService aiProductInfoService;

    @Container
    public BrowserWebDriverContainer<?> browserWebDriverContainer = new BrowserWebDriverContainer<>()
            .withCapabilities(new ChromeOptions())
            .withExtraHost("host.testcontainers.internal", "host-gateway");

    protected WebDriver driver;

    @BeforeEach
    void setupMockAiProductInfoService() {
        when(aiProductInfoService.getAiProductInfo(anyString()))
            .thenReturn(new AiProductInfoService.AiProductInfoDTO("Mocked AI-generated product description for testing."));
        driver = browserWebDriverContainer.getWebDriver();
    }
}
