package com.ozdemir0ozdemir.orderservice.web.controllers;

import com.ozdemir0ozdemir.orderservice.domain.OrderNotFoundException;
import com.ozdemir0ozdemir.orderservice.domain.OrderService;
import com.ozdemir0ozdemir.orderservice.domain.SecurityService;
import com.ozdemir0ozdemir.orderservice.domain.models.CreateOrderRequest;
import com.ozdemir0ozdemir.orderservice.domain.models.CreateOrderResponse;
import com.ozdemir0ozdemir.orderservice.domain.models.OrderDTO;
import com.ozdemir0ozdemir.orderservice.domain.models.OrderSummary;
import jakarta.validation.Valid;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/orders")
public class OrderController {
    private static final Logger log = LoggerFactory.getLogger(OrderController.class);

    private final OrderService orderService;
    private final SecurityService securityService;

    OrderController(OrderService orderService, SecurityService securityService) {
        this.orderService = orderService;
        this.securityService = securityService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    CreateOrderResponse createOrder(@Valid @RequestBody CreateOrderRequest request) {
        String username = securityService.getLoginUsername();
        log.info("Creating order for user: {}", username);

        return orderService.createOrder(username, request);
    }

    @GetMapping
    List<OrderSummary> getOrders() {
        String username = securityService.getLoginUsername();
        log.info("Fetching orders for user: {}", username);
        return orderService.findOrders(username);
    }

    @GetMapping("/{orderNumber}")
    OrderDTO getOrder(@PathVariable String orderNumber) {
        log.info("Fecthing order by number: {}", orderNumber);
        String username = securityService.getLoginUsername();
        return orderService
                .findUserOrders(username, orderNumber)
                .orElseThrow(() -> new OrderNotFoundException(orderNumber));
    }
}
