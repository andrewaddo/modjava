package com.shashi.webapp.controller;

import com.shashi.dataaccess.entity.Product;
import com.shashi.webapp.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Controller
@RequestMapping("/admin/products")
public class ProductController {

    @Autowired
    private ProductService productService;

    @GetMapping("/list")
    public String listProducts(Model model) {
        List<Product> products = productService.getAllProducts();
        model.addAttribute("products", products);
        return "admin/product-list"; // Thymeleaf template name
    }

    @GetMapping("/add")
    public String showAddProductForm(Model model) {
        model.addAttribute("product", new Product());
        return "admin/add-product"; // Thymeleaf template name
    }

    @PostMapping("/add")
    public String addProduct(@ModelAttribute("product") Product product,
                             @RequestParam("productImage") MultipartFile file) throws IOException {
        if (!file.isEmpty()) {
            product.setImage(file.getBytes());
        }
        // Generate a unique product ID if not provided
        if (product.getPid() == null || product.getPid().isEmpty()) {
            product.setPid("P" + UUID.randomUUID().toString().substring(0, 10).toUpperCase());
        }
        productService.saveProduct(product);
        return "redirect:/admin/products/list";
    }

    @GetMapping("/edit/{pid}")
    public String showEditProductForm(@PathVariable String pid, Model model) {
        Optional<Product> product = productService.getProductById(pid);
        if (product.isPresent()) {
            model.addAttribute("product", product.get());
            return "admin/edit-product"; // Thymeleaf template name
        } else {
            return "redirect:/admin/products/list";
        }
    }

    @PostMapping("/edit")
    public String editProduct(@ModelAttribute("product") Product product,
                              @RequestParam(value = "productImage", required = false) MultipartFile file) throws IOException {
        if (file != null && !file.isEmpty()) {
            product.setImage(file.getBytes());
        } else {
            // If no new image is uploaded, retain the existing one
            productService.getProductById(product.getPid()).ifPresent(existingProduct -> {
                product.setImage(existingProduct.getImage());
            });
        }
        productService.updateProduct(product);
        return "redirect:/admin/products/list";
    }

    @GetMapping("/delete/{pid}")
    public String deleteProduct(@PathVariable String pid) {
        productService.deleteProduct(pid);
        return "redirect:/admin/products/list";
    }

    // Endpoint to display product images
    @GetMapping("/image/{pid}")
    @ResponseBody
    public byte[] showProductImage(@PathVariable String pid) {
        Optional<Product> product = productService.getProductById(pid);
        return product.map(Product::getImage).orElse(null);
    }
}
