package com.shoppingcart;

import com.google.genai.Client;
import com.google.genai.types.GenerateContentResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;

@Service
public class AiProductInfoService {

    private static final Logger LOGGER = LoggerFactory.getLogger(AiProductInfoService.class);

    private final Client client;

    // Using constructor injection for better testability
    public AiProductInfoService(Client client) {
        this.client = client;
    }

    public AiProductInfoDTO getAiProductInfo(String productName) {
        if (client == null) {
            return new AiProductInfoDTO("Error: GenAI client not available.");
        }

        try {
            String prompt = "Provide a short, engaging product description for the product named: " + productName;
            GenerateContentResponse response = client.models.generateContent("gemini-1.5-flash", prompt, null);
            return new AiProductInfoDTO(response.text());
        } catch (Exception e) {
            LOGGER.error("Error calling Gemini API for productName: {}", productName, e);
            return new AiProductInfoDTO("Could not retrieve AI-powered product information at this time.");
        }
    }

    // DTO class for returning the product info
    public static class AiProductInfoDTO {
        private String description;

        public AiProductInfoDTO(String description) {
            this.description = description;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }
    }
}
