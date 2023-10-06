package com.craft.onboarding.driveronboarding.OnboardingStateMachine;

import org.springframework.messaging.Message;
import org.springframework.statemachine.listener.StateMachineListenerAdapter;
import org.springframework.statemachine.state.State;
import redis.clients.jedis.Jedis;

public class DeferredEventListener extends StateMachineListenerAdapter {

    Jedis jedis = new Jedis();

    @Override
    public void eventNotAccepted(Message event) {
        //String driverId = event.getHeaders().get("driverId").toString();
        System.out.println("EVVVVVVVVent Accepted");
        jedis.lpush("drivereventNotAccepted:", (String) event.getPayload());
    }

//    @Override
//    public void eventProcessed(Message event) {
//        //String driverId = event.getHeaders().get("driverId").toString();
//        System.out.println("EVVVVVVVVent Accepted");
//        jedis.lpush("drivereventNotAccepted:", (String) event.getPayload());
//    }



    @Override
    public void stateChanged(State from, State to) {
        jedis.lpush("driverStateChangednwo:", (String) from.getId());
        from.getDeferredEvents();

    }
}