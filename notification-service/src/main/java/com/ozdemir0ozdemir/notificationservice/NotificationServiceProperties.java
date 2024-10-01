package com.ozdemir0ozdemir.notificationservice;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "notifications")
public record NotificationServiceProperties(
        String orderEventsExchange,
        String newOrdersQueue,
        String deliveredOrdersQueue,
        String cancelledOrdersQueue,
        String errorOrdersQueue,
        String supportEmail) {}
