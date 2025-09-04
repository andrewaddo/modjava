package com.shoppingcart;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class ProductChatbotService {

    private final ProductRepository productRepository;

    @Autowired
    public ProductChatbotService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public String getProductInfo(String query) {
        // Try to find an exact match by product name first
        List<Product> products = productRepository.searchProducts(query);
        Optional<Product> exactMatch = products.stream()
                                       .filter(p -> p.getName().equalsIgnoreCase(query))
                                       .findFirst();
        if (exactMatch.isPresent()) {
            return formatProductDetails(exactMatch.get());
        }

        // If no exact match, proceed with general search results

        if (products.isEmpty()) {
            return "I couldn't find any products matching '" + query + "'.";
        } else if (products.size() == 1) {
            return formatProductDetails(products.get(0));
        } else {
            StringBuilder sb = new StringBuilder("I found several products matching '" + query + ":\n");
            for (Product product : products) {
                sb.append("- ").append(product.getName()).append(" (Price: $").append(String.format("%.2f", product.getPrice())).append(")\n");
            }
            sb.append("Please specify which product you're interested in for more details.");
            return sb.toString();
        }
    }

    private String formatProductDetails(Product product) {
        return String.format(
                "Product Name: %s\nDescription: %s\nPrice: $%.2f\nAvailable Quantity: %d",
                product.getName(),
                product.getInfo(),
                product.getPrice(),
                product.getQuantity()
        );
    }
}
