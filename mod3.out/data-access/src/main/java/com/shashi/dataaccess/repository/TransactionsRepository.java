package com.shashi.dataaccess.repository;

import com.shashi.dataaccess.entity.Transactions;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TransactionsRepository extends JpaRepository<Transactions, String> {
    List<Transactions> findByUsername(String username);
}
