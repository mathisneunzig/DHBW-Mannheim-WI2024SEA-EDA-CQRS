package com.example.demo.commands;

import com.example.demo.events.EventPublisher;
import com.example.demo.events.domain.ShiggySleptEvent;
import com.example.demo.object.Shiggy;
import org.springframework.stereotype.Component;

@Component
public class SleepCommandHandler{
    private final Shiggy shiggy;
    private final EventPublisher eventPublisher;

    public SleepCommandHandler(Shiggy shiggy, EventPublisher eventPublisher) {
        this.shiggy = shiggy;
        this.eventPublisher = eventPublisher;
    }

    public void handle(){
        int oldSleep = shiggy.getSleep();
        shiggy.setSleep(oldSleep + 5);
        int newSleep = shiggy.getSleep();
        
        ShiggySleptEvent event = new ShiggySleptEvent(oldSleep, newSleep);
        eventPublisher.publish(event);
    }
}
