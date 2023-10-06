package com.craft.onboarding.driveronboarding.OnboardingStateMachine;

import com.craft.onboarding.driveronboarding.config.YamlPropertySourceFactory;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;


@Component
@Configuration
@ConfigurationProperties("machines")
@PropertySource(value = "classpath:state-machines.yaml", factory = YamlPropertySourceFactory.class)
public class StateMachineDefinition {

    private Map<String, Map<String, CustomStateMachine>>stateMachines = new HashMap<>();

    public Map<String, Map<String, CustomStateMachine>> getStateMachines() {
        return stateMachines;
    }

    public void setStateMachines(Map<String, Map<String, CustomStateMachine>> stateMachines) {
        this.stateMachines = stateMachines;
    }

}