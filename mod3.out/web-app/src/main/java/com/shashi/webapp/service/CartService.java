package com.shashi.webapp.service;

import com.shashi.dataaccess.entity.Product;
import com.shashi.dataaccess.entity.Usercart;
import com.shashi.dataaccess.entity.UsercartId;
import com.shashi.dataaccess.repository.ProductRepository;
import com.shashi.dataaccess.repository.UsercartRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CartService {

    @Autowired
    private UsercartRepository usercartRepository;

    @Autowired
    private ProductRepository productRepository;

    public void addProductToCart(String username, String prodid, int quantity) {
        UsercartId id = new UsercartId(username, prodid);
        Optional<Usercart> existingCartItem = usercartRepository.findById(id);

        if (existingCartItem.isPresent()) {
            Usercart cartItem = existingCartItem.get();
            cartItem.setQuantity(cartItem.getQuantity() + quantity);
            usercartRepository.save(cartItem);
        } else {
            Usercart newCartItem = new Usercart(id, quantity);
            usercartRepository.save(newCartItem);
        }
    }

    public void updateProductQuantity(String username, String prodid, int quantity) {
        UsercartId id = new UsercartId(username, prodid);
        Optional<Usercart> existingCartItem = usercartRepository.findById(id);

        if (existingCartItem.isPresent()) {
            Usercart cartItem = existingCartItem.get();
            if (quantity > 0) {
                cartItem.setQuantity(quantity);
                usercartRepository.save(cartItem);
            } else {
                usercartRepository.delete(cartItem);
            }
        }
    }

    public void removeProductFromCart(String username, String prodid) {
        usercartRepository.deleteById_UsernameAndId_Prodid(username, prodid);
    }

    public List<Usercart> getCartItemsByUsername(String username) {
        return usercartRepository.findById_Username(username);
    }

    public BigDecimal getCartTotal(String username) {
        List<Usercart> cartItems = getCartItemsByUsername(username);
        BigDecimal total = BigDecimal.ZERO;
        for (Usercart item : cartItems) {
            Optional<Product> productOptional = productRepository.findById(item.getId().getProdid());
            if (productOptional.isPresent()) {
                Product product = productOptional.get();
                total = total.add(product.getPprice().multiply(BigDecimal.valueOf(item.getQuantity())));
            }
        }
        return total;
    }

    public List<Product> getProductsInCart(String username) {
        List<Usercart> cartItems = getCartItemsByUsername(username);
        return cartItems.stream()
                .map(item -> productRepository.findById(item.getId().getProdid()))
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toList());
    }
}
