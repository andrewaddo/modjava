package com.shashi.webapp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@ComponentScan(basePackages = {"com.shashi.webapp", "com.shashi.service"})
@EntityScan(basePackages = {"com.shashi.dataaccess.entity"})
@EnableJpaRepositories(basePackages = {"com.shashi.dataaccess.repository"})
public class ShoppingCartApplication {

    public static void main(String[] args) {
        SpringApplication.run(ShoppingCartApplication.class, args);
    }
}
