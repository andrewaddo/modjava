
package com.shoppingcart;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import jakarta.validation.Valid;

@Controller
public class ProductController {

    @Autowired
    private ProductService productService;

    

    @GetMapping("/test")
    public String test() {
        return "test";
    }

    @GetMapping("/")
    public String viewHomePage(Model model) {
        model.addAttribute("products", productService.getAllProducts());
        return "index"; // Assuming 'index.html' or 'index.jsp' is the main products page
    }

    @GetMapping("/admin/products")
    public String listProducts(Model model) {
        model.addAttribute("products", productService.getAllProducts());
        return "admin/products";
    }

    @GetMapping("/admin/products/add")
    public String showAddProductForm(Model model) {
        model.addAttribute("product", new Product());
        return "admin/add-product";
    }

    @PostMapping("/admin/products/add")
    public String addProduct(@Valid @ModelAttribute("product") Product product, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            bindingResult.getAllErrors().forEach(error -> System.out.println(error.getDefaultMessage()));
            return "admin/add-product";
        }
        
        return "redirect:/admin/products";
    }

    @GetMapping("/admin/products/edit/{id}")
    public String showUpdateProductForm(@PathVariable("id") String id, Model model) {
        model.addAttribute("product", productService.getProductById(id));
        return "admin/edit-product";
    }

    @PostMapping("/admin/products/edit/{id}")
    public String updateProduct(@PathVariable("id") String id, @Valid @ModelAttribute("product") Product product, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "admin/edit-product";
        }
        
        productService.updateProduct(id, product);
        return "redirect:/admin/products";
    }

    @GetMapping("/admin/products/delete/{id}")
    public String deleteProduct(@PathVariable("id") String id) {
        productService.removeProduct(id);
        return "redirect:/admin/products";
    }

    @GetMapping("/admin/stock")
    public String showStock(Model model) {
        model.addAttribute("products", productService.getAllProducts());
        return "admin/stock";
    }
}
