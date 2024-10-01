package com.ozdemir0ozdemir.orderservice.domain.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.Set;

public record OrderDTO(
        String orderNumber,
        String userName,
        Set<OrderItem> items,
        Customer customer,
        Address deliveryAddress,
        OrderStatus status,
        String comments,
        Instant createdAt) {

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    public BigDecimal getTotalAmount() {
        return items().stream()
                .map(item -> item.price().multiply(new BigDecimal(item.quantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}
