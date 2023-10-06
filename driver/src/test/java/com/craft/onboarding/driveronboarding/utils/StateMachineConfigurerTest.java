package com.craft.onboarding.driveronboarding.utils;

import com.craft.onboarding.driveronboarding.OnboardingStateMachine.*;
import com.craft.onboarding.driveronboarding.enums.VehicleType;
import com.craft.onboarding.driveronboarding.service.TrackingDeviceService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.config.StateMachineBuilder;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.when;

public class StateMachineConfigurerTest {

    @InjectMocks
    private StateMachineConfigurer stateMachineConfigurer;

    @Mock
    private StateMachineDefinition stateMachineDefinitions;

    @Mock
    private TrackingDeviceService trackingDeviceService;

    @Mock
    CustomStateMachine customStateMachine;

    @Mock
    Map<String, Map<String, CustomStateMachine>> map ;

    @Mock
    Map<String, CustomStateMachine> map1;

    @Mock
    StateMachineTransition stateMachineTransition;

    @Mock
    StateMachineBuilder.Builder<String, String> builder;

    @Mock
    StateMachine<String, String> stateMachine;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        ReflectionTestUtils.setField(stateMachineConfigurer, "stateMachineDefinitions", stateMachineDefinitions);
        ReflectionTestUtils.setField(stateMachineConfigurer, "trackingDeviceService", trackingDeviceService);
    }

    @Test
    public void testCreateStateMachine() throws Exception {
        mockStatic(StateMachineConfigurer.class);
        String city = "TestCity";
        VehicleType vehicleType = VehicleType.SUV;
        List<StateMachineTransition> pl = new ArrayList<>();
        StateMachineTransition stateMachineTransition = new StateMachineTransition("s1","s2","e1");
        pl.add(stateMachineTransition);

        //customStateMachine.setInitialState("Initial");
        List<String> l1 = new ArrayList<>();
        List<StateMachineTransition> l2 = new ArrayList<>();
        StateMachineTransition l3 = null;

        List<DeferredStateEvents> l4 = new ArrayList<>();
        customStateMachine.setStates(l1);
        when(customStateMachine.getDeferredStateEvents()).thenReturn(l4);
        customStateMachine.getDeferredStateEvents().add(new DeferredStateEvents("s1","e1"));
        customStateMachine.setTransitions(pl);

        when(StateMachineConfigurer.build(any())).thenReturn(stateMachine);
        when(stateMachineDefinitions.getStateMachines()).thenReturn(map);
        when(map.get(anyString())).thenReturn(map1);
        when(map1.get(any())).thenReturn(customStateMachine);

        StateMachine<String, String> stateMachine = stateMachineConfigurer.createStateMachine(city, vehicleType);

        assertNotNull(stateMachine);
    }


}
