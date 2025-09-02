package com.shoppingcart;

import com.google.genai.Client;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GenAiConfig {

    private static final Logger LOGGER = LoggerFactory.getLogger(GenAiConfig.class);

    @Bean
    public Client genAiClient() {
        // The client automatically looks for the API key in the GOOGLE_API_KEY environment variable.
        // Ensure this environment variable is set in the deployment environment.
        try {
            return new Client();
        } catch (Exception e) {
            LOGGER.error("Error initializing GenAI Client, AI features will be disabled.", e);
            // Return null if the client cannot be initialized. The service will handle this.
            return null;
        }
    }
}
