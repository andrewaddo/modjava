package com.shoppingcart;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ActiveProfiles("h2")
@WebMvcTest(AiProductInfoController.class)
class AiProductInfoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AiProductInfoService aiProductInfoService;

    @Test
    @WithMockUser
    void getAiProductInfo_success() throws Exception {
        String productName = "Test Product";
        String description = "This is a great product.";
        AiProductInfoService.AiProductInfoDTO dto = new AiProductInfoService.AiProductInfoDTO(description);

        when(aiProductInfoService.getAiProductInfo(productName)).thenReturn(dto);

        mockMvc.perform(get("/api/product/{productName}/ai-info", productName))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.description").value(description));
    }

    @Test
    @WithMockUser
    void getAiProductInfo_error() throws Exception {
        String productName = "Test Product";
        String errorMessage = "Could not retrieve AI-powered product information at this time.";
        AiProductInfoService.AiProductInfoDTO dto = new AiProductInfoService.AiProductInfoDTO(errorMessage);

        when(aiProductInfoService.getAiProductInfo(productName)).thenReturn(dto);

        mockMvc.perform(get("/api/product/{productName}/ai-info", productName))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.description").value(errorMessage));
    }
}
