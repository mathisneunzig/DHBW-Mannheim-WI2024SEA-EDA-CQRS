package com.example.demo.readmodel;

import org.springframework.stereotype.Component;
import java.time.LocalDateTime;

@Component
public class ShiggyReadModel {
    private int hunger = 50;
    private int happiness = 50;
    private int sleep = 50;
    private LocalDateTime lastUpdated = LocalDateTime.now();
    
    public int getHunger() {
        return hunger;
    }
    
    public void setHunger(int hunger) {
        this.hunger = hunger;
        this.lastUpdated = LocalDateTime.now();
    }
    
    public int getHappiness() {
        return happiness;
    }
    
    public void setHappiness(int happiness) {
        this.happiness = happiness;
        this.lastUpdated = LocalDateTime.now();
    }
    
    public int getSleep() {
        return sleep;
    }
    
    public void setSleep(int sleep) {
        this.sleep = sleep;
        this.lastUpdated = LocalDateTime.now();
    }
    
    public LocalDateTime getLastUpdated() {
        return lastUpdated;
    }
}
