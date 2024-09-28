package com.ozdemir0ozdemir.orderservice.web.controllers;

import com.ozdemir0ozdemir.orderservice.OrderServiceProperties;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RabbitMQDemoController {

    private final RabbitTemplate rabbitTemplate;
    private final OrderServiceProperties properties;

    RabbitMQDemoController(RabbitTemplate rabbitTemplate, OrderServiceProperties properties) {
        this.rabbitTemplate = rabbitTemplate;
        this.properties = properties;
    }

    @PostMapping("/send")
    public void sendMessage(@RequestBody MyMessage message) {
        rabbitTemplate.convertAndSend(properties.orderEventsExchange(), message.routingKey(), message.payload());
    }

    public record MyMessage(String routingKey, MyPayload payload) {}

    public record MyPayload(String content) {}
}
