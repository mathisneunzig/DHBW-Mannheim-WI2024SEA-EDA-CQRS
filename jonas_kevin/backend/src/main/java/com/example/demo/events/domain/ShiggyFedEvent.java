package com.example.demo.events.domain;

public class ShiggyFedEvent extends DomainEvent {
    private int oldHunger;
    private int newHunger;
    
    // Default constructor für Jackson (JSON Serialisierung)
    public ShiggyFedEvent() {
        super();
    }
    
    public ShiggyFedEvent(int oldHunger, int newHunger) {
        super();
        this.oldHunger = oldHunger;
        this.newHunger = newHunger;
    }
    
    public int getOldHunger() {
        return oldHunger;
    }
    
    public void setOldHunger(int oldHunger) {
        this.oldHunger = oldHunger;
    }
    
    public int getNewHunger() {
        return newHunger;
    }
    
    public void setNewHunger(int newHunger) {
        this.newHunger = newHunger;
    }
}
