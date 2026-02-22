package com.example.demo.events.domain;

public class ShiggyPlayedEvent extends DomainEvent {
    private int oldHappiness;
    private int newHappiness;
    
    // Default constructor für Jackson (JSON Serialisierung)
    public ShiggyPlayedEvent() {
        super();
    }
    
    public ShiggyPlayedEvent(int oldHappiness, int newHappiness) {
        super();
        this.oldHappiness = oldHappiness;
        this.newHappiness = newHappiness;
    }
    
    public int getOldHappiness() {
        return oldHappiness;
    }
    
    public void setOldHappiness(int oldHappiness) {
        this.oldHappiness = oldHappiness;
    }
    
    public int getNewHappiness() {
        return newHappiness;
    }
    
    public void setNewHappiness(int newHappiness) {
        this.newHappiness = newHappiness;
    }
}
