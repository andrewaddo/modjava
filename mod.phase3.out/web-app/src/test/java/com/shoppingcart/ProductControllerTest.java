package com.shoppingcart;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import org.springframework.security.test.context.support.WithMockUser;

@WebMvcTest(ProductController.class)
@Import(SecurityConfig.class)
public class ProductControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProductService productService;

    @MockBean
    private AiProductInfoService aiProductInfoService;

    @MockBean
    private UserRepository userRepository;

    @Test
    @WithMockUser
    public void whenViewProductsPageWithoutSearch_thenReturnsAllProducts() throws Exception {
        // given
        Product product = new Product("1", "image.jpg", "info", "name", 10.0, 10);
        when(productService.getAllProducts()).thenReturn(Collections.singletonList(product));

        // when & then
        mockMvc.perform(get("/products"))
                .andExpect(status().isOk())
                .andExpect(view().name("index"))
                .andExpect(model().attributeExists("products"))
                .andExpect(model().attribute("products", Collections.singletonList(product)));
    }

    @Test
    public void whenViewProductsPageWithSearch_thenReturnsSearchedProducts() throws Exception {
        // given
        String keyword = "test";
        Product product = new Product("1", "image.jpg", "info", "test product", 10.0, 10);
        when(productService.searchProducts(keyword)).thenReturn(Collections.singletonList(product));

        // when & then
        mockMvc.perform(get("/products").param("search", keyword))
                .andExpect(status().isOk())
                .andExpect(view().name("index"))
                .andExpect(model().attributeExists("products"))
                .andExpect(model().attribute("products", Collections.singletonList(product)))
                .andExpect(model().attribute("noProductsFound", false));
    }

    @Test
    public void whenViewProductsPageWithSearchAndNoResults_thenReturnsEmptyList() throws Exception {
        // given
        String keyword = "empty";
        when(productService.searchProducts(keyword)).thenReturn(Collections.emptyList());

        // when & then
        mockMvc.perform(get("/products").param("search", keyword))
                .andExpect(status().isOk())
                .andExpect(view().name("index"))
                .andExpect(model().attributeExists("products"))
                .andExpect(model().attribute("products", Collections.emptyList()))
                .andExpect(model().attribute("noProductsFound", true));
    }
}