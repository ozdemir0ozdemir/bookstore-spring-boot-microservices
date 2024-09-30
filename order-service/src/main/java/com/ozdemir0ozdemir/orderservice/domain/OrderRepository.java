package com.ozdemir0ozdemir.orderservice.domain;

import com.ozdemir0ozdemir.orderservice.domain.models.OrderStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

interface OrderRepository extends JpaRepository<OrderEntity, Long> {

    List<OrderEntity> findByStatus(OrderStatus orderStatus);

    Optional<OrderEntity> findByOrderNumber(String orderNumber);

    default void updateOrderStatus(String orderNumber, OrderStatus status) {
        OrderEntity order = this.findByOrderNumber(orderNumber).orElseThrow();
        order.setStatus(status);
        this.save(order);
    }
}
