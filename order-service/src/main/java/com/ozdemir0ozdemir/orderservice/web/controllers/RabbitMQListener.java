package com.ozdemir0ozdemir.orderservice.web.controllers;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

@Service
public class RabbitMQListener {

    @RabbitListener(queues = "${orders.new-orders-queue}")
    public void handleNewOrder(RabbitMQDemoController.MyPayload payload) {
        System.out.println("New Order: " + payload.content());
    }

    @RabbitListener(queues = "${orders.delivered-orders-queue}")
    public void handleDeliveredOrder(RabbitMQDemoController.MyPayload payload) {
        System.out.println("Order Delivered: " + payload.content());
    }
}
