package com.example.demo.commands;

import com.example.demo.events.EventPublisher;
import com.example.demo.events.domain.ShiggyFedEvent;
import com.example.demo.object.Shiggy;
import org.springframework.stereotype.Component;

@Component
public class FeedCommandHandler {
    private final Shiggy shiggy;
    private final EventPublisher eventPublisher;

    public FeedCommandHandler(Shiggy shiggy, EventPublisher eventPublisher) {
        this.shiggy = shiggy;
        this.eventPublisher = eventPublisher;
    }

    public void handle() {
        int oldHunger = shiggy.getHunger();
        shiggy.setHunger(oldHunger + 5);
        int newHunger = shiggy.getHunger();
        
        ShiggyFedEvent event = new ShiggyFedEvent(oldHunger, newHunger);
        eventPublisher.publish(event);
    }
}
