package com.shashi.webapp.controller;

import com.shashi.dataaccess.entity.ProductEntity;
import com.shashi.service.product.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/admin/products")
public class AdminProductController {

    @Autowired
    private ProductService productService;

    @GetMapping("/add")
    public String showAddProductForm(Model model) {
        model.addAttribute("product", new ProductEntity());
        return "admin/addProduct";
    }

    @PostMapping("/add")
    public String addProduct(@ModelAttribute("product") ProductEntity product,
                             @RequestParam("productImage") MultipartFile file) throws IOException {
        if (!file.isEmpty()) {
            product.setImage(file.getBytes());
        }
        productService.addProduct(product);
        return "redirect:/admin/products/view";
    }

    @GetMapping("/view")
    public String viewProducts(Model model) {
        List<ProductEntity> products = productService.getAllProducts();
        model.addAttribute("products", products);
        return "admin/viewProduct";
    }

    @GetMapping("/edit/{pid}")
    public String showEditProductForm(@PathVariable("pid") String pid, Model model) {
        Optional<ProductEntity> product = productService.getProductById(pid);
        if (product.isPresent()) {
            model.addAttribute("product", product.get());
            return "admin/updateProduct";
        } else {
            return "redirect:/admin/products/view";
        }
    }

    @PostMapping("/edit")
    public String updateProduct(@ModelAttribute("product") ProductEntity product,
                                @RequestParam(value = "productImage", required = false) MultipartFile file) throws IOException {
        if (file != null && !file.isEmpty()) {
            product.setImage(file.getBytes());
        } else {
            // Retain existing image if no new file is uploaded
            Optional<ProductEntity> existingProduct = productService.getProductById(product.getPid());
            existingProduct.ifPresent(productEntity -> product.setImage(productEntity.getImage()));
        }
        productService.updateProduct(product);
        return "redirect:/admin/products/view";
    }

    @GetMapping("/delete/{pid}")
    public String deleteProduct(@PathVariable("pid") String pid) {
        productService.deleteProduct(pid);
        return "redirect:/admin/products/view";
    }

    @GetMapping("/stock")
    public String showStockManagement(Model model) {
        List<ProductEntity> products = productService.getAllProducts();
        model.addAttribute("products", products);
        return "admin/stockProduct";
    }

    @PostMapping("/stock/update")
    public String updateStock(@RequestParam("pid") String pid,
                              @RequestParam("quantity") Integer quantity) {
        productService.updateProductQuantity(pid, quantity);
        return "redirect:/admin/products/stock";
    }
}
