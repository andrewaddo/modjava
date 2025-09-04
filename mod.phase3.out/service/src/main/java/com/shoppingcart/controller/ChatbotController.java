package com.shoppingcart.controller;

import com.google.genai.Client;
import com.google.genai.types.Content;
import com.google.genai.types.Part;
import com.google.genai.types.GenerateContentConfig; // New import
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;
import java.util.HashMap;

@RestController
@RequestMapping("/api/chatbot")
public class ChatbotController {

    private static final Logger LOGGER = LoggerFactory.getLogger(ChatbotController.class);

    private final Client genAiClient;

    @Autowired
    public ChatbotController(Client genAiClient) {
        this.genAiClient = genAiClient;
    }

    @PostMapping
    public Map<String, String> sendMessage(@RequestBody Map<String, String> request) {
        String userMessage = request.get("message");
        Map<String, String> response = new HashMap<>();

        if (genAiClient == null) {
            response.put("response", "Chatbot service is currently unavailable. Please try again later.");
            return response;
        }

        try {
            // Use the models field of the client to generate content
            String aiResponse = genAiClient.models.generateContent("gemini-1.5-flash", userMessage, null).text();
            response.put("response", aiResponse);
        } catch (Exception e) {
            // Log the error for debugging
            LOGGER.error("Error processing chatbot request: {}", e.getMessage(), e);
            response.put("response", "I'm sorry, I encountered an error trying to process your request. Please try again.");
        }
        return response;
    }
}
