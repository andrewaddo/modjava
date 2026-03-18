package com.shoppingcart.controller;

import com.google.genai.Client;
import com.google.genai.types.Content;
import com.google.genai.types.Part;
import com.google.genai.types.GenerateContentConfig; // New import
import com.shoppingcart.ProductChatbotService; // Import ProductChatbotService
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;
import java.util.HashMap;
import com.fasterxml.jackson.databind.ObjectMapper; // For JSON parsing
import com.fasterxml.jackson.core.JsonProcessingException; // For JSON parsing

@RestController
@RequestMapping("/api/chatbot")
public class ChatbotController {

    private static final Logger LOGGER = LoggerFactory.getLogger(ChatbotController.class);
    private static final ObjectMapper objectMapper = new ObjectMapper(); // For JSON parsing

    private final Client genAiClient;
    private final ProductChatbotService productChatbotService; // Inject ProductChatbotService

    @Autowired
    public ChatbotController(Client genAiClient, ProductChatbotService productChatbotService) {
        this.genAiClient = genAiClient;
        this.productChatbotService = productChatbotService;
    }

    @PostMapping
    public Map<String, String> sendMessage(@RequestBody Map<String, String> request) {
        String userMessage = request.get("message");
        Map<String, String> response = new HashMap<>();
        String intentResponseJson = null; // Declare outside try block

        if (genAiClient == null) {
            response.put("response", "Chatbot service is currently unavailable. Please try again later.");
            return response;
        }

        try {
            // Step 1: Use GenAI for intent and entity extraction
            String intentPrompt = "Analyze the following user message and determine its intent. " +
                                  "If it's a product inquiry, extract the product name/keywords. " +
                                  "Respond in JSON format with 'intent' (e.g., 'product_inquiry', 'general_question') " +
                                  "and 'product_query' (if applicable, otherwise null). " +
                                  "IMPORTANT: Use double quotes for all JSON keys and string values. " +
                                  "Example: {\"intent\": \"product_inquiry\", \"product_query\": \"Laptop Pro\"}\n" +
                                  "Message: " + userMessage;

            intentResponseJson = genAiClient.models.generateContent("gemini-1.5-flash", intentPrompt, null).text();
            
            // Clean up the JSON string: replace single quotes with double quotes for robust parsing
            intentResponseJson = intentResponseJson.replace('"', '"');
            // Also remove backticks and "json" if present (already doing this)
            intentResponseJson = intentResponseJson.replace("```json", "").replace("```", "").trim();

            Map<String, Object> intentData = objectMapper.readValue(intentResponseJson, Map.class);
            String intent = null;
            Object intentObj = intentData.get("intent");
            if (intentObj instanceof String) {
                intent = (String) intentObj;
            } else if (intentObj instanceof java.util.ArrayList) {
                java.util.ArrayList<?> intentList = (java.util.ArrayList<?>) intentObj;
                if (!intentList.isEmpty() && intentList.get(0) instanceof String) {
                    intent = (String) intentList.get(0);
                }
            }

            String productQuery = null;
            Object productQueryObj = intentData.get("product_query");
            if (productQueryObj instanceof String) {
                productQuery = (String) productQueryObj;
            } else if (productQueryObj instanceof java.util.ArrayList) {
                java.util.ArrayList<?> productQueryList = (java.util.ArrayList<?>) productQueryObj;
                if (!productQueryList.isEmpty() && productQueryList.get(0) instanceof String) {
                    productQuery = (String) productQueryList.get(0);
                }
            }
            LOGGER.info("Product query: {}", productQuery);

            if ("product_inquiry".equals(intent) && productQuery != null && !productQuery.isEmpty()) {
                // Step 2: Retrieve product information
                String productInfo = productChatbotService.getProductInfo(productQuery);

                // Step 3: Use GenAI to generate a comprehensive response based on product info
                String finalResponsePrompt = "The user asked: \"" + userMessage + "\". " +
                                             "Here is the product information: \"" + productInfo + "\". " +
                                             "Generate a helpful and concise response based on this information. " +
                                             "If the product was not found, inform the user politely.";
                String aiResponse = genAiClient.models.generateContent("gemini-1.5-flash", finalResponsePrompt, null).text();
                response.put("response", aiResponse);
            } else {
                // Step 4: General question, use GenAI directly
                String aiResponse = genAiClient.models.generateContent("gemini-1.5-flash", userMessage, null).text();
                response.put("response", aiResponse);
            }

        } catch (JsonProcessingException e) {
            LOGGER.error("Error parsing GenAI intent response JSON: {}", intentResponseJson, e);
            response.put("response", "I'm sorry, I had trouble understanding your request due to a processing error. Please try again.");
        } catch (Exception e) {
            LOGGER.error("Error processing chatbot request: {}", e.getMessage(), e);
            response.put("response", "I'm sorry, I encountered an error trying to process your request. Please try again.");
        }
        return response;
    }
}

