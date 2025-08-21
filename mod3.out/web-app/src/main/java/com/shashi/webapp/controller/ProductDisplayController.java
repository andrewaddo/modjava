package com.shashi.webapp.controller;

import com.shashi.dataaccess.entity.Product;
import com.shashi.webapp.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class ProductDisplayController {

    @Autowired
    private ProductService productService;

    @GetMapping("/products")
    public String listAllProducts(Model model) {
        List<Product> products = productService.getAllProducts();
        model.addAttribute("products", products);
        return "product-list-user"; // Thymeleaf template name for user view
    }
}
