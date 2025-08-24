package com.shoppingcart;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class DemandService {

    @Autowired
    private DemandRepository demandRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ProductRepository productRepository;

    @Transactional
    public Demand addDemand(String username, String prodid, int demandQty) {
        DemandPK id = new DemandPK(username, prodid);
        Optional<Demand> existingDemand = demandRepository.findById(id);

        if (existingDemand.isPresent()) {
            Demand demand = existingDemand.get();
            demand.setDemandQty(demand.getDemandQty() + demandQty);
            return demandRepository.save(demand);
        } else {
            Demand newDemand = new Demand(username, prodid, demandQty);
            // Set User and Product objects for the new demand
            userRepository.findById(username).ifPresent(newDemand::setUser);
            productRepository.findById(prodid).ifPresent(newDemand::setProduct);
            return demandRepository.save(newDemand);
        }
    }

    @Transactional
    public void removeDemand(String username, String prodid) {
        DemandPK id = new DemandPK(username, prodid);
        demandRepository.deleteById(id);
    }

    public List<Demand> getDemandsByProdid(String prodid) {
        return demandRepository.findByProdid(prodid);
    }

    @Transactional
    public void deleteDemands(List<Demand> demands) {
        demandRepository.deleteAll(demands);
    }
}
