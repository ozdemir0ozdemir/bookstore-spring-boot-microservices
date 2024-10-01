package com.ozdemir0ozdemir.notificationservice.domain.models;

import java.time.Instant;
import java.util.Set;

public record OrderErrorEvent(
        String eventId,
        String orderNumber,
        Set<OrderItem> items,
        Customer customer,
        Address deliveryAddress,
        String reason,
        Instant createdAt) {}
