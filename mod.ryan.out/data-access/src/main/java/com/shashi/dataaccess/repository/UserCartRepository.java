package com.shashi.dataaccess.repository;

import com.shashi.dataaccess.entity.UserCartEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserCartRepository extends JpaRepository<UserCartEntity, Long> {
    List<UserCartEntity> findByUsername(String username);
    UserCartEntity findByUsernameAndProdid(String username, String prodid);
    void deleteByUsernameAndProdid(String username, String prodid);
    void deleteByUsername(String username);
}
