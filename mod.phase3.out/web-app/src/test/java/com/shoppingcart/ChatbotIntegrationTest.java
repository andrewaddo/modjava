package com.shoppingcart;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles; // Re-added import
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf; // New import

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("h2") // Activating H2 profile for this test
public class ChatbotIntegrationTest extends BaseIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void testChatbotEndpointReturnsResponse() throws Exception {
        String userMessage = "Hello, chatbot!";
        mockMvc.perform(post("/api/chatbot")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"message\": \"" + userMessage + "\"}")
                .with(csrf())) // Added CSRF token
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.response").exists());
    }
}
