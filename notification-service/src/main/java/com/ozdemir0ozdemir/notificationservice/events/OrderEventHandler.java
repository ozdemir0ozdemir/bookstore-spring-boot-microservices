package com.ozdemir0ozdemir.notificationservice.events;

import com.ozdemir0ozdemir.notificationservice.domain.NotificationService;
import com.ozdemir0ozdemir.notificationservice.domain.OrderEventEntity;
import com.ozdemir0ozdemir.notificationservice.domain.OrderEventRepository;
import com.ozdemir0ozdemir.notificationservice.domain.models.OrderCancelledEvent;
import com.ozdemir0ozdemir.notificationservice.domain.models.OrderCreatedEvent;
import com.ozdemir0ozdemir.notificationservice.domain.models.OrderDeliveredEvent;
import com.ozdemir0ozdemir.notificationservice.domain.models.OrderErrorEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class OrderEventHandler {
    private static final Logger log = LoggerFactory.getLogger(OrderEventHandler.class);

    private final NotificationService notificationService;
    private final OrderEventRepository orderEventRepository;

    OrderEventHandler(NotificationService notificationService, OrderEventRepository orderEventRepository) {
        this.notificationService = notificationService;
        this.orderEventRepository = orderEventRepository;
    }

    @RabbitListener(queues = "${notifications.new-orders-queue}")
    void handleOrderEvent(OrderCreatedEvent event) {
        if (isEventIdProcessedBefore(event.eventId())) {
            log.warn("Received Duplicated OrderCreatedEvent with eventId: {}", event.eventId());
            return;
        }

        log.info("Order Created Event: {}", event);
        notificationService.sendOrderNotification(event);
        saveEventToDb(event.eventId());
    }

    @RabbitListener(queues = "${notifications.delivered-orders-queue}")
    void handleOrderEvent(OrderDeliveredEvent event) {
        if (isEventIdProcessedBefore(event.eventId())) {
            log.warn("Received Duplicated OrderDeliveredEvent with eventId: {}", event.eventId());
            return;
        }

        log.info("Order Delivered Event: {}", event);
        notificationService.sendOrderNotification(event);
        saveEventToDb(event.eventId());
    }

    @RabbitListener(queues = "${notifications.cancelled-orders-queue}")
    void handleOrderEvent(OrderCancelledEvent event) {
        if (isEventIdProcessedBefore(event.eventId())) {
            log.warn("Received Duplicated OrderCancelledEvent with eventId: {}", event.eventId());
            return;
        }
        log.info("Order Cancelled Event: {}", event);
        notificationService.sendOrderNotification(event);
        saveEventToDb(event.eventId());
    }

    @RabbitListener(queues = "${notifications.error-orders-queue}")
    void handleOrderEvent(OrderErrorEvent event) {
        if (isEventIdProcessedBefore(event.eventId())) {
            log.warn("Received Duplicated OrderErrorEvent with eventId: {}", event.eventId());
            return;
        }
        log.error("Order Error Event: {}", event);
        notificationService.sendOrderNotification(event);
        saveEventToDb(event.eventId());
    }

    // Private Helpers
    private boolean isEventIdProcessedBefore(String eventId) {
        return orderEventRepository.existsByEventId(eventId);
    }

    private void saveEventToDb(String eventId) {
        OrderEventEntity entity = new OrderEventEntity(eventId);
        orderEventRepository.save(entity);
    }
}
