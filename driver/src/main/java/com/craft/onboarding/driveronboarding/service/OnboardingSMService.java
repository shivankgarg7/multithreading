package com.craft.onboarding.driveronboarding.service;

import com.craft.onboarding.driveronboarding.model.Driver;
import org.springframework.http.ResponseEntity;



public interface OnboardingSMService {



    public ResponseEntity<String> markStartOnboarding(int driverId);
    public ResponseEntity<Driver> passApprovedEventToOSM(int driverId) ;

    public ResponseEntity<Driver> passCompletedTrainingVideosEventToOSM(int driverId);

    public ResponseEntity<Driver> passTrackingDeviceDeliveredEventToOSM(int driverId) ;

    public ResponseEntity<Driver> markingReady(int driverId);

    Driver getDriver(int driverId);
}
