package com.shashi.dataaccess.repository;

import com.shashi.dataaccess.entity.Usercart;
import com.shashi.dataaccess.entity.UsercartId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UsercartRepository extends JpaRepository<Usercart, UsercartId> {
    List<Usercart> findById_Username(String username);
    void deleteById_UsernameAndId_Prodid(String username, String prodid);
}
