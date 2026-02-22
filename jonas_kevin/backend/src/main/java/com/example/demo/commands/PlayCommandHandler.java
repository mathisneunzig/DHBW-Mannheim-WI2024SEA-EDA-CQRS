package com.example.demo.commands;

import com.example.demo.events.EventPublisher;
import com.example.demo.events.domain.ShiggyPlayedEvent;
import com.example.demo.object.Shiggy;
import org.springframework.stereotype.Component;

@Component
public class PlayCommandHandler {
    private final Shiggy shiggy;
    private final EventPublisher eventPublisher;

    public PlayCommandHandler(Shiggy shiggy, EventPublisher eventPublisher) {
        this.shiggy = shiggy;
        this.eventPublisher = eventPublisher;
    }

    public void handle() {
        int oldHappiness = shiggy.getHappiness();
        shiggy.setHappiness(oldHappiness + 5);
        int newHappiness = shiggy.getHappiness();
        
        ShiggyPlayedEvent event = new ShiggyPlayedEvent(oldHappiness, newHappiness);
        eventPublisher.publish(event);
    }
}
