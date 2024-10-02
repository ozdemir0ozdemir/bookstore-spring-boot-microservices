package com.ozdemir0ozdemir.bookstorewebapp.client.orders;

import java.util.List;
import java.util.Map;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.service.annotation.GetExchange;
import org.springframework.web.service.annotation.PostExchange;

public interface OrderServiceClient {

    @PostExchange("/orders/api/v1/orders")
    OrderConfirmationDTO createOrder(@RequestHeader Map<String, ?> headers, @RequestBody CreateOrderRequest request);

    @GetExchange("/orders/api/v1/orders")
    List<OrderSummary> getOrders(@RequestHeader Map<String, ?> headers);

    @GetExchange("/orders/api/v1/orders/{orderNumber}")
    OrderDTO getOrder(@RequestHeader Map<String, ?> headers, @PathVariable String orderNumber);
}
