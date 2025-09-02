package com.shoppingcart;

import com.google.genai.Client;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.testcontainers.junit.jupiter.Testcontainers;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = ShoppingCartApplication.class)
@Testcontainers
@ActiveProfiles("test")
public abstract class BaseIntegrationTest {

    @MockBean
    protected Client genAiClient;

}
