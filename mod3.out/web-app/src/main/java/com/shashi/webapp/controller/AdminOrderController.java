package com.shashi.webapp.controller;

import com.shashi.dataaccess.entity.Orders;
import com.shashi.webapp.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequestMapping("/admin/orders")
public class AdminOrderController {

    @Autowired
    private OrderService orderService;

    @GetMapping("/list")
    public String listAllOrders(Model model) {
        List<Orders> orders = orderService.getAllOrders();
        model.addAttribute("orders", orders);
        return "admin/order-list"; // Thymeleaf template
    }

    @GetMapping("/shipped")
    public String listShippedOrders(Model model) {
        List<Orders> orders = orderService.getOrdersByShippedStatus(1);
        model.addAttribute("orders", orders);
        model.addAttribute("status", "Shipped");
        return "admin/order-list";
    }

    @GetMapping("/unshipped")
    public String listUnshippedOrders(Model model) {
        List<Orders> orders = orderService.getOrdersByShippedStatus(0);
        model.addAttribute("orders", orders);
        model.addAttribute("status", "Unshipped");
        return "admin/order-list";
    }

    @PostMapping("/updateStatus")
    public String updateOrderStatus(@RequestParam("orderId") String orderId,
                                    @RequestParam("prodid") String prodid,
                                    @RequestParam("shipped") int shippedStatus) {
        orderService.updateOrderStatus(orderId, prodid, shippedStatus);
        return "redirect:/admin/orders/list";
    }
}
