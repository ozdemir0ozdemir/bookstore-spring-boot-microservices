package com.ozdemir0ozdemir.bookstorewebapp.web.controller;

import com.ozdemir0ozdemir.bookstorewebapp.client.orders.*;
import jakarta.validation.Valid;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class OrderController {
    private static final Logger log = LoggerFactory.getLogger(OrderController.class);
    private final OrderServiceClient orderServiceClient;

    OrderController(OrderServiceClient orderServiceClient) {
        this.orderServiceClient = orderServiceClient;
    }

    @GetMapping("/cart")
    String cart() {
        return "cart";
    }

    @GetMapping("/orders/{orderNumber}")
    String showOrderDetails(@PathVariable String orderNumber, Model model) {
        model.addAttribute("orderNumber", orderNumber);
        return "order_details";
    }

    @GetMapping("/orders")
    String showOrders() {
        return "orders";
    }

    // REST API CALLS
    @PostMapping("/api/orders")
    @ResponseBody
    OrderConfirmationDTO createOrder(@Valid @RequestBody CreateOrderRequest request) {
        log.info("Creating order: {}", request);
        return orderServiceClient.createOrder(getHeaders(), request);
    }

    @GetMapping("/api/orders/{orderNumber}")
    @ResponseBody
    OrderDTO getOrder(@PathVariable String orderNumber) {
        log.info("Fetching order details for orderNumber: {}", orderNumber);
        return orderServiceClient.getOrder(getHeaders(), orderNumber);
    }

    @GetMapping("/api/orders")
    @ResponseBody
    List<OrderSummary> getOrders() {
        log.info("Fetching orders.");
        return orderServiceClient.getOrders(getHeaders());
    }

    private Map<String, ?> getHeaders() {
        return Map.of();
    }
}
