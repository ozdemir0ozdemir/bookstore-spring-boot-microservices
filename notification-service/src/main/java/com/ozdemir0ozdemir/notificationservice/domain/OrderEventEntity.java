package com.ozdemir0ozdemir.notificationservice.domain;

import jakarta.persistence.*;
import java.time.Instant;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

@Entity
@Table(name = "order_events")
public class OrderEventEntity {

    @Id
    @SequenceGenerator(name = "order_events_id_gen", sequenceName = "order_event_id_seq")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "order_events_id_gen")
    private Long id;

    @Column(name = "event_id", nullable = false, unique = true)
    private String eventId;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private Instant updatedAt;

    public OrderEventEntity() {}

    public OrderEventEntity(String eventId) {
        this.eventId = eventId;
    }

    // Getters & Setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEventId() {
        return eventId;
    }

    public void setEventId(String eventId) {
        this.eventId = eventId;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Instant updatedAt) {
        this.updatedAt = updatedAt;
    }
}
