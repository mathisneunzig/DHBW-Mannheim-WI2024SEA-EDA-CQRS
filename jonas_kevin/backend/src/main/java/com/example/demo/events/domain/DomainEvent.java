package com.example.demo.events.domain;

import java.time.LocalDateTime;
import java.util.UUID;

public abstract class DomainEvent {
    private final UUID eventId;
    private final LocalDateTime timestamp;
    
    protected DomainEvent() {
        this.eventId = UUID.randomUUID();
        this.timestamp = LocalDateTime.now();
    }
    
    public UUID getEventId() {
        return eventId;
    }
    
    public LocalDateTime getTimestamp() {
        return timestamp;
    }
}
