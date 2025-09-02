package com.shoppingcart;

import org.junit.jupiter.api.BeforeEach;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.mockito.Mockito.when;
import static org.mockito.ArgumentMatchers.anyString;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = ShoppingCartApplication.class)
@Testcontainers
@ActiveProfiles("test")
public abstract class BaseIntegrationTest {

    @MockBean
    protected AiProductInfoService aiProductInfoService;

    @BeforeEach
    void setupMockAiProductInfoService() {
        when(aiProductInfoService.getAiProductInfo(anyString()))
            .thenReturn(new AiProductInfoService.AiProductInfoDTO("Mocked AI-generated product description for testing."));
    }
}
