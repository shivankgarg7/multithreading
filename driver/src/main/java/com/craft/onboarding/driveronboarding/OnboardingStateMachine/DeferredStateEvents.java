package com.craft.onboarding.driveronboarding.OnboardingStateMachine;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class DeferredStateEvents {
    private String state;
    private String event;

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getEvent() {
        return event;
    }

    public void setEvent(String event) {
        this.event = event;
    }
}
