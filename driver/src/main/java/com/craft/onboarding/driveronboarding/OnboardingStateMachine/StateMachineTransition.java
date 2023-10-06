package com.craft.onboarding.driveronboarding.OnboardingStateMachine;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class StateMachineTransition {
    private String from;
    private String to;
    private String event;

    // Getters and setters for all properties

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public String getEvent() {
        return event;
    }

    public void setEvent(String event) {
        this.event = event;
    }

    @Override
    public String toString(){
        return from + " " +to + " "+ event;
    }
}