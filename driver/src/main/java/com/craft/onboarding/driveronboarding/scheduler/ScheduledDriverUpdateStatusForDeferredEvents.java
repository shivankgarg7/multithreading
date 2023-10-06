package com.craft.onboarding.driveronboarding.scheduler;

import com.craft.onboarding.driveronboarding.model.Driver;
import com.craft.onboarding.driveronboarding.service.DriverService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

//@EnableScheduling
public class ScheduledDriverUpdateStatusForDeferredEvents {

    @Autowired
    DriverService driverService;

    //@Scheduled(fixedDelay = 1000)
    public void updateStatusForDeferredEvents() {
        List<Driver> drivers = driverService.getAllOnboardingDrivers();

        for(Driver driver: drivers){
             //load all drivers SMs use deferred event list and update SMs

        }


    }

}
