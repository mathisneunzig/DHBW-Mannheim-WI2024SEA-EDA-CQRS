package com.example.demo.object;

import org.springframework.stereotype.Component;

@Component
public class Shiggy {
    private int hunger = 50;
    private int happiness = 50;
    private int sleep = 50;

    public int getHunger() {
        return hunger;
    }

    public void setHunger(int hunger) {
        this.hunger = hunger;
    }

    public int getHappiness() {
        return happiness;
    }

    public void setHappiness(int happiness) {
        this.happiness = happiness;
    }

    public int getSleep() {
        return sleep;
    }

    public void setSleep(int sleep) {
        this.sleep = sleep;
    }
}