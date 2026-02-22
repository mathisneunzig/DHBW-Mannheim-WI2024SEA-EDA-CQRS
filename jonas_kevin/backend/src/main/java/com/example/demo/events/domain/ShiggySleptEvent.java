package com.example.demo.events.domain;

public class ShiggySleptEvent extends DomainEvent {
    private int oldSleep;
    private int newSleep;
    
    // Default constructor für Jackson (JSON Serialisierung)
    public ShiggySleptEvent() {
        super();
    }
    
    public ShiggySleptEvent(int oldSleep, int newSleep) {
        super();
        this.oldSleep = oldSleep;
        this.newSleep = newSleep;
    }
    
    public int getOldSleep() {
        return oldSleep;
    }
    
    public void setOldSleep(int oldSleep) {
        this.oldSleep = oldSleep;
    }
    
    public int getNewSleep() {
        return newSleep;
    }
    
    public void setNewSleep(int newSleep) {
        this.newSleep = newSleep;
    }
}
