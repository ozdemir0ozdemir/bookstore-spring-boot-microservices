package com.ozdemir0ozdemir.orderservice.domain;

import com.ozdemir0ozdemir.orderservice.domain.models.*;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class OrderService {

    private static final Logger log = LoggerFactory.getLogger(OrderService.class);
    private static final List<String> DELIVERY_ALLOWED_COUNTRIES = List.of("TÜRKİYE", "NORWAY", "USA", "UK");

    private final OrderRepository orderRepository;
    private final OrderValidator orderValidator;
    private final OrderEventService orderEventService;

    OrderService(OrderRepository orderRepository, OrderValidator orderValidator, OrderEventService orderEventService) {
        this.orderRepository = orderRepository;
        this.orderValidator = orderValidator;
        this.orderEventService = orderEventService;
    }

    public CreateOrderResponse createOrder(String username, CreateOrderRequest request) {
        // Validate and prepare the request
        orderValidator.validate(request);
        OrderEntity newOrder = OrderMapper.convertToEntity(request);
        newOrder.setUserName(username);

        // Save the create order request
        OrderEntity savedOrder = this.orderRepository.save(newOrder);
        log.info("Created Order with Order Number: {}", savedOrder.getOrderNumber());

        // Create OrderCreatedEvent from the saved order
        // save ->
        // 1. Convert OrderCreatedEvent to OrderEventEntity
        // 2. Set entity's event type to CREATED
        // 3. Set entity's payload to OrderCreatedEvent itself
        // 4. Save the OrderEvent entity to the database
        OrderCreatedEvent orderCreatedEvent = OrderEventMapper.buildOrderCreatedEvent(savedOrder);
        orderEventService.save(orderCreatedEvent);

        return new CreateOrderResponse(savedOrder.getOrderNumber());
    }

    public List<OrderSummary> findOrders(String username) {
        return orderRepository.findByUserName(username);
    }

    public Optional<OrderDTO> findUserOrders(String username, String orderNumber) {
        return orderRepository
                .findByUserNameAndOrderNumber(username, orderNumber)
                .map(OrderMapper::convertToDTO);
    }

    public void processNewOrders() {
        List<OrderEntity> orders = orderRepository.findByStatus(OrderStatus.NEW);
        log.info("Found {} new orders to process", orders.size());
        for (var order : orders) {
            this.process(order);
        }
    }

    private void process(OrderEntity order) {
        try {
            if (canBeDelivered(order)) {
                log.info("OrderNumber: {} can be delivered.", order.getOrderNumber());
                orderRepository.updateOrderStatus(order.getOrderNumber(), OrderStatus.DELIVERED);
                orderEventService.save(OrderEventMapper.buildOrderDeliveredEvent(order));
            } else {
                log.info("OrderNumber: {} cannot be delivered.", order.getOrderNumber());
                orderRepository.updateOrderStatus(order.getOrderNumber(), OrderStatus.CANCELLED);
                orderEventService.save(
                        OrderEventMapper.buildOrderCancelledEvent(order, "Can't deliver to the location"));
            }
        } catch (RuntimeException ex) {
            log.info("Failed to process Order with orderNumber: {}", order.getOrderNumber());
            orderRepository.updateOrderStatus(order.getOrderNumber(), OrderStatus.ERROR);
            orderEventService.save(OrderEventMapper.buildOrderErrorEvent(order, ex.getMessage()));
        }
    }

    // FIXME: Dummy Logic to simulate the program. - canBeDelivered(OrderEntity order) -
    private boolean canBeDelivered(OrderEntity order) {
        return DELIVERY_ALLOWED_COUNTRIES.contains(
                order.getDeliveryAddress().country().toUpperCase());
    }
}
