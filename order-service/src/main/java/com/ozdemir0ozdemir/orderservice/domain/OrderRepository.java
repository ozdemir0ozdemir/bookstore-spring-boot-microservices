package com.ozdemir0ozdemir.orderservice.domain;

import com.ozdemir0ozdemir.orderservice.domain.models.OrderStatus;
import com.ozdemir0ozdemir.orderservice.domain.models.OrderSummary;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

interface OrderRepository extends JpaRepository<OrderEntity, Long> {

    List<OrderEntity> findByStatus(OrderStatus orderStatus);

    Optional<OrderEntity> findByOrderNumber(String orderNumber);

    default void updateOrderStatus(String orderNumber, OrderStatus status) {
        OrderEntity order = this.findByOrderNumber(orderNumber).orElseThrow();
        order.setStatus(status);
        this.save(order);
    }

    @Query(
            """
        select new com.ozdemir0ozdemir.orderservice.domain.models.OrderSummary(o.orderNumber, o.status)
        from OrderEntity o
        where o.userName = :userName
        """)
    List<OrderSummary> findByUserName(String userName);

    @Query(
            """
           select distinct o
           from OrderEntity o left join fetch o.items
           where o.userName = :username and o.orderNumber = :orderNumber
           """)
    Optional<OrderEntity> findByUserNameAndOrderNumber(String username, String orderNumber);
}
