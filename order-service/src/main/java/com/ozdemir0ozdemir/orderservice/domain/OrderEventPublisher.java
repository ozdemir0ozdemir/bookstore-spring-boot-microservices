package com.ozdemir0ozdemir.orderservice.domain;

import com.ozdemir0ozdemir.orderservice.OrderServiceProperties;
import com.ozdemir0ozdemir.orderservice.domain.models.OrderCancelledEvent;
import com.ozdemir0ozdemir.orderservice.domain.models.OrderCreatedEvent;
import com.ozdemir0ozdemir.orderservice.domain.models.OrderDeliveredEvent;
import com.ozdemir0ozdemir.orderservice.domain.models.OrderErrorEvent;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

@Component
public class OrderEventPublisher {

    private final RabbitTemplate rabbitTemplate;
    private final OrderServiceProperties properties;

    public OrderEventPublisher(RabbitTemplate rabbitTemplate, OrderServiceProperties properties) {
        this.rabbitTemplate = rabbitTemplate;
        this.properties = properties;
    }

    public void publish(OrderCreatedEvent event) {
        this.send(properties.newOrdersQueue(), event);
    }

    public void publish(OrderDeliveredEvent event) {
        this.send(properties.deliveredOrdersQueue(), event);
    }

    public void publish(OrderCancelledEvent event) {
        this.send(properties.cancelledOrdersQueue(), event);
    }

    public void publish(OrderErrorEvent event) {
        this.send(properties.errorOrdersQueue(), event);
    }

    private void send(String routingKey, Object payload) {
        rabbitTemplate.convertAndSend(properties.orderEventsExchange(), routingKey, payload);
    }
}
