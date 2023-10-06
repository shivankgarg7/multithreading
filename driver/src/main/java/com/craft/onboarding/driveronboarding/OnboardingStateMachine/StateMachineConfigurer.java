package com.craft.onboarding.driveronboarding.OnboardingStateMachine;

import com.craft.onboarding.driveronboarding.enums.VehicleType;
import com.craft.onboarding.driveronboarding.service.TrackingDeviceService;
import com.craft.onboarding.driveronboarding.utils.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.config.StateMachineBuilder;
import org.springframework.stereotype.Component;

import java.util.HashSet;

@Component
public class StateMachineConfigurer {

//    @Autowired
//    private StateMachineFactory<String, String> stateMachineFactory;


    @Autowired
    private StateMachineDefinition stateMachineDefinitions;

    @Autowired
    private TrackingDeviceService trackingDeviceService;

    @SuppressWarnings("ConstantConditions")
    public StateMachine<String, String> createStateMachine(String city, VehicleType vehicleType) throws Exception {
        StateMachineBuilder.Builder<String, String> builder = StateMachineBuilder.builder();

        //Adding State List
        CustomStateMachine csm = stateMachineDefinitions.getStateMachines().get(city).get(vehicleType.name());
        builder.configureStates()
                .withStates()
                .initial(csm.getInitalState())
                .states(new HashSet<>(csm.getStates()));

        //Adding Deferred States
        for(DeferredStateEvents dse: csm.getDeferredStateEvents()){
            builder.configureStates()
                    .withStates()
                    .state(dse.getState(),dse.getEvent());
        }
        builder.configureStates()
                .withStates()
                .stateEntry(Constants.CAN_SHIP_TRACKING_DEVICE, context -> trackingDeviceService.sendTrackingDevice((Integer) context.getMessageHeaders().get("driverId")));


        // Add additional transitions dynamically based on the definition
        for (int i = 0; i < stateMachineDefinitions.getStateMachines().get(city).get(vehicleType.name()).getTransitions().size(); i++) {
            builder.configureTransitions()
                    .withExternal()
                    .source(csm.getTransitions().get(i).getFrom())
                    .target(csm.getTransitions().get(i).getTo())
                    .event(csm.getTransitions().get(i).getEvent());
        }

        StateMachine<String, String> stateMachine = build(builder);
        stateMachine.addStateListener(new DeferredEventListener());
        return stateMachine;
    }

    public static StateMachine<String, String> build(StateMachineBuilder.Builder<String, String> builder){
        return builder.build();
    }


}
