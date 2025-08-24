package com.shoppingcart;

import java.util.List;

public interface TransactionService {
    Transaction createTransaction(User user, List<UserCartItem> userCartItems); // Renamed method
    List<Transaction> getTransactionsByUser(User user); // Renamed method
    Transaction getTransactionById(String transactionId); // Renamed method
}