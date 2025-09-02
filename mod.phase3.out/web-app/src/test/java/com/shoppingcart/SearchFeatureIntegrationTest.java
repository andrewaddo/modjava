package com.shoppingcart;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class SearchFeatureIntegrationTest extends BaseIntegrationTest {

    @Autowired
    private WebApplicationContext webApplicationContext;

    private MockMvc mockMvc;

    @Autowired
    private ProductRepository productRepository;

    @Container
    public static PostgreSQLContainer<?> postgreSQLContainer = new PostgreSQLContainer<>("postgres:13");

    @DynamicPropertySource
    static void databaseProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgreSQLContainer::getJdbcUrl);
        registry.add("spring.datasource.username", postgreSQLContainer::getUsername);
        registry.add("spring.datasource.password", postgreSQLContainer::getPassword);
    }

    @BeforeEach
    public void setup() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.webApplicationContext).build();
        productRepository.deleteAll();
    }

    @Test
    public void whenSearchForProduct_thenProductIsDisplayed() throws Exception {
        // given
        Product product = new Product("1", "image.jpg", "A book about testing", "Spring Boot Testing", 50.00, 10);
        productRepository.save(product);

        // when & then
        mockMvc.perform(get("/products").param("search", "testing"))
                .andExpect(status().isOk())
                .andExpect(view().name("index"))
                .andExpect(model().attributeExists("products"))
                .andExpect(content().string(containsString("Spring Boot Testing")));
    }

    @Test
    public void whenSearchForNonExistentProduct_thenNoProductsFoundMessageIsDisplayed() throws Exception {
        // given
        Product product = new Product("1", "image.jpg", "A book about testing", "Spring Boot Testing", 50.00, 10);
        productRepository.save(product);

        // when & then
        mockMvc.perform(get("/products").param("search", "java"))
                .andExpect(status().isOk())
                .andExpect(view().name("index"))
                .andExpect(model().attribute("noProductsFound", true))
                .andExpect(content().string(containsString("No products found")));
    }
}
