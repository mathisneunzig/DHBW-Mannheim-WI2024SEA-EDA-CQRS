package com.example.demo.readmodel;

import com.example.demo.events.domain.*;
import org.springframework.stereotype.Component;

@Component
public class ShiggyReadModelUpdater {
    
    private final ShiggyReadModel readModel;
    
    public ShiggyReadModelUpdater(ShiggyReadModel readModel) {
        this.readModel = readModel;
    }
    
    public void updateFromFedEvent(ShiggyFedEvent event) {
        readModel.setHunger(event.getNewHunger());
        System.out.println("Read model updated: hunger = " + event.getNewHunger());
    }
    
    public void updateFromPlayedEvent(ShiggyPlayedEvent event) {
        readModel.setHappiness(event.getNewHappiness());
        System.out.println("Read model updated: happiness = " + event.getNewHappiness());
    }
    
    public void updateFromSleptEvent(ShiggySleptEvent event) {
        readModel.setSleep(event.getNewSleep());
        System.out.println("Read model updated: sleep = " + event.getNewSleep());
    }
}
