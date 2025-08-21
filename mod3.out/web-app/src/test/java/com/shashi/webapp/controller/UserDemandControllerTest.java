package com.shashi.webapp.controller;

import com.shashi.dataaccess.entity.UserDemand;
import com.shashi.dataaccess.entity.UserDemandId;
import com.shashi.dataaccess.repository.UserDemandRepository;
import com.shashi.webapp.config.TestSecurityConfig;
import com.shashi.webapp.service.ProductService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UserDemandController.class)
@Import(TestSecurityConfig.class)
public class UserDemandControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserDemandRepository userDemandRepository;

    @MockBean
    private ProductService productService;

    private final String TEST_USERNAME = "user@example.com";
    private final String TEST_PRODID = "P001";

    @Test
    @WithMockUser(username = TEST_USERNAME)
    void testDemandProduct_NewDemand() throws Exception {
        UserDemandId id = new UserDemandId(TEST_USERNAME, TEST_PRODID);
        UserDemand userDemand = new UserDemand(id, 1);

        when(userDemandRepository.existsById(id)).thenReturn(false);
        when(userDemandRepository.save(any(UserDemand.class))).thenReturn(userDemand);

        mockMvc.perform(post("/demandProduct")
                        .param("prodid", TEST_PRODID)
                        .param("quantity", "1"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/products"))
                .andExpect(flash().attributeExists("message"));

        verify(userDemandRepository, times(1)).existsById(id);
        verify(userDemandRepository, times(1)).save(any(UserDemand.class));
    }

    @Test
    @WithMockUser(username = TEST_USERNAME)
    void testDemandProduct_ExistingDemand() throws Exception {
        UserDemandId id = new UserDemandId(TEST_USERNAME, TEST_PRODID);

        when(userDemandRepository.existsById(id)).thenReturn(true);

        mockMvc.perform(post("/demandProduct")
                        .param("prodid", TEST_PRODID)
                        .param("quantity", "1"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/products"))
                .andExpect(flash().attributeExists("message"));

        verify(userDemandRepository, times(1)).existsById(id);
        verify(userDemandRepository, never()).save(any(UserDemand.class));
    }
}
