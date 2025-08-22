package com.shashi.service.cart;

import com.shashi.dataaccess.entity.UserCartEntity;
import com.shashi.dataaccess.repository.UserCartRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class CartServiceImpl implements CartService {

    @Autowired
    private UserCartRepository userCartRepository;

    @Override
    public UserCartEntity addProductToCart(String username, String prodid, Integer quantity) {
        UserCartEntity cartItem = userCartRepository.findByUsernameAndProdid(username, prodid);
        if (cartItem == null) {
            cartItem = new UserCartEntity();
            cartItem.setUsername(username);
            cartItem.setProdid(prodid);
            cartItem.setQuantity(quantity);
        } else {
            cartItem.setQuantity(cartItem.getQuantity() + quantity);
        }
        return userCartRepository.save(cartItem);
    }

    @Override
    public UserCartEntity updateProductQuantityInCart(String username, String prodid, Integer quantity) {
        UserCartEntity cartItem = userCartRepository.findByUsernameAndProdid(username, prodid);
        if (cartItem != null) {
            cartItem.setQuantity(quantity);
            return userCartRepository.save(cartItem);
        }
        return null; // Or throw an exception
    }

    @Override
    @Transactional
    public void removeProductFromCart(String username, String prodid) {
        userCartRepository.deleteByUsernameAndProdid(username, prodid);
    }

    @Override
    public List<UserCartEntity> getCartItemsByUsername(String username) {
        return userCartRepository.findByUsername(username);
    }

    @Override
    @Transactional
    public void clearCart(String username) {
        userCartRepository.deleteByUsername(username);
    }
}
