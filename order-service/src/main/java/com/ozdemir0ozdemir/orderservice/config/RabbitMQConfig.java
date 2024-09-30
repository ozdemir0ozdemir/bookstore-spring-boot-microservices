package com.ozdemir0ozdemir.orderservice.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ozdemir0ozdemir.orderservice.OrderServiceProperties;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    private final OrderServiceProperties orderServiceProperties;

    public RabbitMQConfig(OrderServiceProperties config) {
        this.orderServiceProperties = config;
    }

    @Bean
    DirectExchange exchange() {
        return new DirectExchange(orderServiceProperties.orderEventsExchange());
    }

    @Bean
    Queue newOrdersQueue() {
        return QueueBuilder.durable(orderServiceProperties.newOrdersQueue()).build();
    }

    @Bean
    Binding newOrdersQueueBinding() {
        return BindingBuilder.bind(newOrdersQueue()).to(exchange()).with(orderServiceProperties.newOrdersQueue());
    }

    @Bean
    Queue deliveredOrdersQueue() {
        return QueueBuilder.durable(orderServiceProperties.deliveredOrdersQueue())
                .build();
    }

    @Bean
    Binding deliveredOrdersQueueBinding() {
        return BindingBuilder.bind(deliveredOrdersQueue())
                .to(exchange())
                .with(orderServiceProperties.deliveredOrdersQueue());
    }

    @Bean
    Queue cancelledOrdersQueue() {
        return QueueBuilder.durable(orderServiceProperties.cancelledOrdersQueue())
                .build();
    }

    @Bean
    Binding cancelledOrdersQueueBinding() {
        return BindingBuilder.bind(cancelledOrdersQueue())
                .to(exchange())
                .with(orderServiceProperties.cancelledOrdersQueue());
    }

    @Bean
    Queue errorOrdersQueue() {
        return QueueBuilder.durable(orderServiceProperties.errorOrdersQueue()).build();
    }

    @Bean
    Binding errorOrdersQueueBinding() {
        return BindingBuilder.bind(errorOrdersQueue()).to(exchange()).with(orderServiceProperties.errorOrdersQueue());
    }

    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory, ObjectMapper objectMapper) {
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMessageConverter(jacksonConverter(objectMapper));
        return rabbitTemplate;
    }

    @Bean
    public Jackson2JsonMessageConverter jacksonConverter(ObjectMapper objectMapper) {
        return new Jackson2JsonMessageConverter(objectMapper);
    }
}
