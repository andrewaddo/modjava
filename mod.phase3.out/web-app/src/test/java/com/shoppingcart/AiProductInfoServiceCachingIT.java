package com.shoppingcart;

import com.google.genai.Client;
import com.google.genai.Models;
import com.google.genai.types.GenerateContentResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.cache.CacheManager;
import org.springframework.test.context.ActiveProfiles;

import java.lang.reflect.Field;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ActiveProfiles("h2")
@SpringBootTest
class AiProductInfoServiceCachingIT {

    @MockBean
    private Client mockClient;

    @MockBean
    private Models mockModels;

    @MockBean
    private GenerateContentResponse mockResponse;

    @Autowired
    private AiProductInfoService aiProductInfoService;

    @Autowired
    private CacheManager cacheManager;

    @BeforeEach
    void setUp() throws Exception {
        Field modelsField = Client.class.getDeclaredField("models");
        modelsField.setAccessible(true);
        modelsField.set(mockClient, mockModels);
        cacheManager.getCache("aiProductInfo").clear();
    }

    @Test
    void getAiProductInfo_caching() throws Exception {
        // Arrange
        String productName = "Test Product Caching";
        String expectedDescription = "This is a fantastic product for caching.";
        when(mockModels.generateContent(anyString(), anyString(), any())).thenReturn(mockResponse);
        when(mockResponse.text()).thenReturn(expectedDescription);

        // Act
        aiProductInfoService.getAiProductInfo(productName);
        aiProductInfoService.getAiProductInfo(productName);

        // Assert
        verify(mockModels, times(1)).generateContent(anyString(), anyString(), any());
    }
}