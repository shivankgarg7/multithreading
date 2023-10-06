package com.craft.onboarding.driveronboarding.service;

import com.craft.onboarding.driveronboarding.OnboardingStateMachine.StateMachineConfigurer;
import com.craft.onboarding.driveronboarding.enums.Events;
import com.craft.onboarding.driveronboarding.exception.DriverOnboardingException;
import com.craft.onboarding.driveronboarding.model.Driver;
import com.craft.onboarding.driveronboarding.model.TrackingDevice;
import com.craft.onboarding.driveronboarding.utils.Constants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.data.redis.RedisStateMachinePersister;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;


@Service
@Slf4j
public class OnboardingSMServiceImpl implements OnboardingSMService {

    @Autowired
    DriverService driverService;

    @Autowired
    StateMachineConfigurer stateMachineConfigurer;

    @Autowired
    VehicleService vehicleService;

    @Autowired
    RedisStateMachinePersister redisStateMachinePersister;

    @Autowired
    TrackingDeviceService trackingDeviceService;

    @Override
    public ResponseEntity<String> markStartOnboarding(int driverId){

        Driver driver = passEventsToOSM(driverId, Events.START_APPROVAL, true);
        return  ResponseEntity.ok("Onboarding_started");
    }

    @Override

    public ResponseEntity<Driver> passApprovedEventToOSM(int driverId) {
        Driver driver = passEventsToOSM(driverId, Events.APPROVED, false);
        return ResponseEntity.ok(driver);


    }

    @Override

    public ResponseEntity<Driver> passCompletedTrainingVideosEventToOSM(int driverId) {
        Driver driver = passEventsToOSM(driverId, Events.SEEN_TRAINING_VIDEOS, false);
        return ResponseEntity.ok(driver);
    }

    @Override

    public ResponseEntity<Driver> passTrackingDeviceDeliveredEventToOSM(int driverId)  {
        Driver driver = passEventsToOSM(driverId, Events.TRACKING_DEVICE_DELIVERED, false);
        trackingDeviceService.trackingDeviceDelivered(driverId);
        return ResponseEntity.ok(driver);
    }

    @Override
    public ResponseEntity<Driver> markingReady(int driverId){

        //if state is can_markread or (state is senn tv and tracking device is delivered) then update db send ok
        //else can not mark ready now
        Driver driver = driverService.getDriver(driverId);
        StateMachine sm  = null;
        try {
            sm = stateMachineConfigurer.createStateMachine(driver.getCity(), vehicleService.getVehicleType(driverId));
            redisStateMachinePersister.restore(sm, "driver:"+driverId);

        } catch (Exception e) {
            throw new DriverOnboardingException("Issue with DRiver Onboarding contact support");
        }
        if(sm == null){
            return new ResponseEntity<>(driver, HttpStatus.FORBIDDEN);
        }

        log.info("Current driver state" + sm.getState().getId() + " driverid:"+driverId);

        TrackingDevice trackingDevice = trackingDeviceService.getTrackingDevice(driverId);

        if(sm.getState().getId().toString().compareTo(Constants.CAN_MARK_READY_STATE) == 0
                || (sm.getState().getId().toString().compareTo(Constants.TRAINING_VIDEOS_SEEN) == 0 && trackingDevice.isDelivered() ) ){

            log.info("Sending Marking ready event to Driver SM driverId: "+ driverId);

            sm.sendEvent(Mono.just(MessageBuilder
                    .withPayload(Events.MARKING_READY.name()).setHeader("driverId", driverId).build())).subscribe();

            log.info("Driver can mark ready driverId: "+ driverId);
            driver.setReadyToTakeRides(true);
            driver.setState(sm.getState().getId().toString());
            driverService.save(driver);
            sm.startReactively().block();

            try {
                redisStateMachinePersister.persist(sm, "driver:" + driverId);
            } catch (Exception e) {
                throw new DriverOnboardingException("Issue with DRiver Onboarding contact support");
            }

            return ResponseEntity.ok(driver);
            //Notify Ride Management Service
        }

        return new ResponseEntity<>(driver, HttpStatus.FORBIDDEN);

    }


    private Driver passEventsToOSM(int driverId, Events event, boolean ifOnboarding){
        Driver driver = driverService.getDriver(driverId);
        StateMachine sm;
        try {
            sm = stateMachineConfigurer.createStateMachine(driver.getCity(), vehicleService.getVehicleType(driverId));
            if (!ifOnboarding)
                redisStateMachinePersister.restore(sm, "driver:" + driverId);
            sm.startReactively().block();
            sm.sendEvent(Mono.just(MessageBuilder
                    .withPayload(event.name()).setHeader("driverId", driverId).build())).subscribe();
            redisStateMachinePersister.persist(sm, "driver:" + driverId);
        }
        catch(Exception ex){
            throw new DriverOnboardingException("Issue with DRiver Onboarding contact support");
        }
        driver.setState(sm.getState().getId().toString());
        if(ifOnboarding){
            driver.setHasOnboardingStarted(true);
        }
        driverService.save(driver);
        return driver;

    }

    @Override
    public Driver getDriver(int driverId) {
        return driverService.getDriver(driverId);
    }
}

