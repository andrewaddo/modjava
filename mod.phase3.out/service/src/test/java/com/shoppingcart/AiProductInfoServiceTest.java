package com.shoppingcart;

import com.google.genai.Client;
import com.google.genai.Models;
import com.google.genai.types.GenerateContentResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.lang.reflect.Field;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AiProductInfoServiceTest {

    @Mock
    private Client mockClient;

    @Mock
    private Models mockModels;

    @Mock
    private GenerateContentResponse mockResponse;

    private AiProductInfoService aiProductInfoService;

    @BeforeEach
    void setUp() {
        aiProductInfoService = new AiProductInfoService(mockClient);
    }

    @Test
    void getAiProductInfo_success() throws Exception {
        // Arrange
        String expectedDescription = "This is a fantastic product.";

        Field modelsField = Client.class.getDeclaredField("models");
        modelsField.setAccessible(true);
        modelsField.set(mockClient, mockModels);

        when(mockModels.generateContent(anyString(), anyString(), any())).thenReturn(mockResponse);
        when(mockResponse.text()).thenReturn(expectedDescription);

        // Act
        AiProductInfoService.AiProductInfoDTO result = aiProductInfoService.getAiProductInfo("Test Product Name");

        // Assert
        assertNotNull(result);
        assertEquals(expectedDescription, result.getDescription());
    }

    @Test
    void getAiProductInfo_apiError() throws Exception {
        // Arrange
        Field modelsField = Client.class.getDeclaredField("models");
        modelsField.setAccessible(true);
        modelsField.set(mockClient, mockModels);

        when(mockModels.generateContent(anyString(), anyString(), any())).thenThrow(new RuntimeException("API Error"));

        // Act
        AiProductInfoService.AiProductInfoDTO result = aiProductInfoService.getAiProductInfo("Test Product Name");

        // Assert
        assertNotNull(result);
        assertEquals("Could not retrieve AI-powered product information at this time.", result.getDescription());
    }

    @Test
    void getAiProductInfo_clientNotInitialized() {
        // Arrange
        aiProductInfoService = new AiProductInfoService(null);

        // Act
        AiProductInfoService.AiProductInfoDTO result = aiProductInfoService.getAiProductInfo("Test Product Name");

        // Assert
        assertNotNull(result);
        assertEquals("Error: GenAI client not available.", result.getDescription());
    }
}
