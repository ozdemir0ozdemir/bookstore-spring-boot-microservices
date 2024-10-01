package com.ozdemir0ozdemir.notificationservice.domain.models;

import java.time.Instant;
import java.util.Set;

public record OrderDeliveredEvent(
        String eventId,
        String orderNumber,
        Set<OrderItem> items,
        Customer customer,
        Address deliveryAddress,
        Instant createdAt) {}
