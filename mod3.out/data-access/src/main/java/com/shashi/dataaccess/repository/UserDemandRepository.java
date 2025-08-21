package com.shashi.dataaccess.repository;

import com.shashi.dataaccess.entity.UserDemand;
import com.shashi.dataaccess.entity.UserDemandId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserDemandRepository extends JpaRepository<UserDemand, UserDemandId> {
    List<UserDemand> findById_Prodid(String prodid);
}
