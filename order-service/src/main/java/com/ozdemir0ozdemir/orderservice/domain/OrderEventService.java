package com.ozdemir0ozdemir.orderservice.domain;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ozdemir0ozdemir.orderservice.domain.models.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class OrderEventService {

    private static final Logger logger = LoggerFactory.getLogger(OrderEventService.class);

    private final OrderEventRepository orderEventRepository;
    private final OrderEventPublisher orderEventPublisher;
    private final ObjectMapper objectMapper;

    OrderEventService(OrderEventRepository orderEventRepository, OrderEventPublisher orderEventPublisher, ObjectMapper objectMapper) {
        this.orderEventRepository = orderEventRepository;
        this.orderEventPublisher = orderEventPublisher;
        this.objectMapper = objectMapper;
    }

    void save(OrderCreatedEvent event){
        var entity = new OrderEventEntity();
        entity.setEventId(event.eventId());
        entity.setEventType(OrderEventType.ORDER_CREATED);
        entity.setOrderNumber(event.orderNumber());
        entity.setCreatedAt(event.createdAt());
        entity.setPayload(toJsonPayload(event));
        this.orderEventRepository.save(entity);
    }

    void save(OrderCancelledEvent event){
        var entity = new OrderEventEntity();
        entity.setEventId(event.eventId());
        entity.setEventType(OrderEventType.ORDER_CANCELLED);
        entity.setOrderNumber(event.orderNumber());
        entity.setCreatedAt(event.createdAt());
        entity.setPayload(toJsonPayload(event));
        this.orderEventRepository.save(entity);
    }

    void save(OrderDeliveredEvent event){
        var entity = new OrderEventEntity();
        entity.setEventId(event.eventId());
        entity.setEventType(OrderEventType.ORDER_DELIVERED);
        entity.setOrderNumber(event.orderNumber());
        entity.setCreatedAt(event.createdAt());
        entity.setPayload(toJsonPayload(event));
        this.orderEventRepository.save(entity);
    }

    void save(OrderErrorEvent event){
        var entity = new OrderEventEntity();
        entity.setEventId(event.eventId());
        entity.setEventType(OrderEventType.ORDER_PROCESSING_FAILED);
        entity.setOrderNumber(event.orderNumber());
        entity.setCreatedAt(event.createdAt());
        entity.setPayload(toJsonPayload(event));
        this.orderEventRepository.save(entity);
    }

    public void publishOrderEvents(){
        Sort sort = Sort.by("createdAt").ascending();
        // Just a few entities.
        List<OrderEventEntity> events = orderEventRepository.findAll(sort);
        logger.info("Found {} Order Events to be published", events.size());
        for(var event: events){
            this.publishEvent(event);
            orderEventRepository.delete(event);
        }
    }

    private void publishEvent(OrderEventEntity entity) {
        OrderEventType eventType = entity.getEventType();
        switch (eventType) {
            case ORDER_CREATED:
                var orderCreatedEvent = fromJsonPayload(entity.getPayload(), OrderCreatedEvent.class);
                orderEventPublisher.publish(orderCreatedEvent);
                break;
            case ORDER_DELIVERED:
                var orderDeliveredEvent = fromJsonPayload(entity.getPayload(), OrderDeliveredEvent.class);
                orderEventPublisher.publish(orderDeliveredEvent);
                break;
            case ORDER_CANCELLED:
                var orderCancelledEvent = fromJsonPayload(entity.getPayload(), OrderCancelledEvent.class);
                orderEventPublisher.publish(orderCancelledEvent);
                break;
            case ORDER_PROCESSING_FAILED:
                var orderErrorEvent = fromJsonPayload(entity.getPayload(), OrderErrorEvent.class);
                orderEventPublisher.publish(orderErrorEvent);
                break;
            default:
                logger.warn("Unsupported OrderEventType: {}", eventType);
        }
    }

    private String toJsonPayload(Object object) {
        try {
            return objectMapper.writeValueAsString(object);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    private <T> T fromJsonPayload(String json, Class<T> type){
        try {
            return objectMapper.readValue(json, type);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
