package com.craft.onboarding.driveronboarding.OnboardingStateMachine;

import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class CustomStateMachine {

//    private String name;
//    private String city;
//    private String vehicleType;
    private List<String> states;
    private List<StateMachineTransition> transitions;

    private List<DeferredStateEvents> deferredStateEvents;

    public List<DeferredStateEvents> getDeferredStateEvents() {
        return deferredStateEvents;
    }

    public void setDeferredStateEvents(List<DeferredStateEvents> deferredStateEvents) {
        this.deferredStateEvents = deferredStateEvents;
    }

    public List<StateMachineTransition> getTransitions() {
        return transitions;
    }

    public void setTransitions(List<StateMachineTransition> transitions) {
        this.transitions = transitions;
    }

    public List<String> getStates() {
        return states;
    }

    public void setStates(List<String> states) {
        this.states = states;
    }

//    public String getName() {
//        return name;
//    }
//
//    public void setName(String name) {
//        this.name = name;
//    }
//
//    public String getCity() {
//        return city;
//    }
//
//    public void setCity(String city) {
//        this.city = city;
//    }
//
//    public String getVehicleType() {
//        return vehicleType;
//    }
//
//    public void setVehicleType(String vehicleType) {
//        this.vehicleType = vehicleType;
//    }

    public String getInitalState(){
        return states.get(0);
    }

    public String toString(){
        return  states.toString() +" "+transitions.toString();
    }

}
