package com.ozdemir0ozdemir.orderservice.domain.models;

import java.time.Instant;
import java.util.Set;

public record OrderCreatedEvent(
        String eventId,
        String orderNumber,
        Set<OrderItem> items,
        Customer customer,
        Address deliveryAddress,
        Instant createdAt) {
}