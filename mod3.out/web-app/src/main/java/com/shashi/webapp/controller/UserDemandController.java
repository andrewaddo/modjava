package com.shashi.webapp.controller;

import com.shashi.dataaccess.entity.UserDemand;
import com.shashi.dataaccess.entity.UserDemandId;
import com.shashi.webapp.service.ProductService;
import com.shashi.dataaccess.repository.UserDemandRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class UserDemandController {

    @Autowired
    private UserDemandRepository userDemandRepository;

    @Autowired
    private ProductService productService;

    private String getCurrentUsername() {
        return SecurityContextHolder.getContext().getAuthentication().getName();
    }

    @PostMapping("/demandProduct")
    public String demandProduct(@RequestParam("prodid") String prodid,
                                @RequestParam(value = "quantity", defaultValue = "1") int quantity,
                                RedirectAttributes redirectAttributes) {
        String username = getCurrentUsername();
        UserDemandId id = new UserDemandId(username, prodid);

        if (userDemandRepository.existsById(id)) {
            redirectAttributes.addFlashAttribute("message", "You have already expressed demand for this product.");
        } else {
            UserDemand userDemand = new UserDemand(id, quantity);
            userDemandRepository.save(userDemand);
            redirectAttributes.addFlashAttribute("message", "Demand for product recorded. You will be notified when it's back in stock.");
        }
        return "redirect:/products";
    }
}
