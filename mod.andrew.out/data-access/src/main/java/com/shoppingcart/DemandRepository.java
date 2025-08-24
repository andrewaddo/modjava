package com.shoppingcart;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface DemandRepository extends JpaRepository<Demand, DemandPK> {
    List<Demand> findByProdid(String prodid);
}