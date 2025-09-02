
package com.shoppingcart;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.List;

import jakarta.validation.Valid;

@Controller
public class ProductController {

    private static final Logger logger = LoggerFactory.getLogger(ProductController.class);

    @Autowired
    private ProductService productService;

    @Autowired
    private AiProductInfoService aiProductInfoService;

    @GetMapping("/test")
    public String test() {
        return "test";
    }

    @GetMapping("/")
    public String viewHomePage(Model model) {
        model.addAttribute("products", productService.getAllProducts());
        return "index"; // Assuming 'index.html' or 'index.jsp' is the main products page
    }

    @GetMapping("/products")
    public String viewProductsPage(Model model, @RequestParam(name = "search", required = false) String keyword) {
        List<Product> products;
        if (keyword != null && !keyword.isEmpty()) {
            products = productService.searchProducts(keyword);
            model.addAttribute("noProductsFound", products.isEmpty());
        } else {
            products = productService.getAllProducts();
        }
        model.addAttribute("products", products);
        return "index";
    }

    @GetMapping("/admin/products")
    public String listProducts(Model model) {
        List<Product> products = productService.getAllProducts();
        logger.info("Number of products: {}", products.size());
        products.forEach(product -> logger.info("Product: {}", product.getPid()));
        model.addAttribute("products", products);
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
        productService.addProduct(product, product.getImage());
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

    @GetMapping("/product-details/{id}")
    public String showProductDetails(@PathVariable("id") String id, Model model) {
        Product product = productService.getProductById(id);
        if (product == null) {
            // Handle product not found, e.g., return a 404 page or redirect
            return "error/404"; // Assuming you have a 404 error page
        }
        model.addAttribute("product", product);
        
        // Add AI-powered product info
        AiProductInfoService.AiProductInfoDTO aiInfo = aiProductInfoService.getAiProductInfo(product.getName());
        model.addAttribute("aiProductInfo", aiInfo);
        
        return "product-details";
    }
}
