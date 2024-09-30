package com.ozdemir0ozdemir.orderservice.jobs;

import com.ozdemir0ozdemir.orderservice.domain.OrderEventService;
import java.time.Instant;
import net.javacrumbs.shedlock.core.LockAssert;
import net.javacrumbs.shedlock.spring.annotation.SchedulerLock;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class OrderEventsPublishingJob {
    private static final Logger log = LoggerFactory.getLogger(OrderEventsPublishingJob.class);

    private final OrderEventService orderEventService;

    OrderEventsPublishingJob(OrderEventService orderEventService) {
        this.orderEventService = orderEventService;
    }

    @Scheduled(cron = "${orders.publish-order-events-job-cron}")
    @SchedulerLock(name = "publishOrderEvents")
    public void publishOrderEvents() {
        LockAssert.assertLocked();
        log.info("Publishing Order Events at {}", Instant.now());

        // publishOrderEvents -->
        // 1. Fetch all OrderEventEntities from db sorting by createdAt column ascending order
        // 2. publishEvent -->
        //      1. Filter events by its type which is CREATED using switch statement.
        //      2. Extract each entity's payloads as a OrderCreatedEvent (ObjectMapper)
        //      3. Send to the OrderEventPublisher to publish it to the rabbitmq
        // 3. Since it published to the rabbitmq, delete the entity from the db
        orderEventService.publishOrderEvents();
    }
}
