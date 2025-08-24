package com.shoppingcart;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/css/**")
                .addResourceLocations("file:./shopping-cart/WebContent/css/");
        registry.addResourceHandler("/images/**")
                .addResourceLocations("file:./shopping-cart/WebContent/images/");
        // Add other static resource locations if needed
    }
}
