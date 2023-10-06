package com.craft.onboarding.driveronboarding.controller;

import com.craft.onboarding.driveronboarding.model.Driver;
import com.craft.onboarding.driveronboarding.service.DriverService;
import com.craft.onboarding.driveronboarding.service.OnboardingSMService;
import com.craft.onboarding.driveronboarding.utils.JwtTokenUtility;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;


@RestController
@Validated
@RequestMapping("/v1")
public class OnboardingController {

    @Autowired
    OnboardingSMService onboardingStateMachineService;

    @Autowired
    JwtTokenUtility jwtTokenUtility;

    @Autowired
    DriverService driverService;

    //For the Approvers/Admin
    @GetMapping("/admin/driver")
    public ResponseEntity<Driver> getDriver(@RequestParam @Valid int driverId){
        Driver driver = onboardingStateMachineService.getDriver(driverId);
        return ResponseEntity.ok(driver);
    }

    //For the Approvers/Admin
    @GetMapping("/admin/driver/approve")
    public ResponseEntity<Driver> approveEvent(@RequestParam @Valid  int driverId) {
        return onboardingStateMachineService.passApprovedEventToOSM(driverId);
    }

    @GetMapping("/auth/start_onboarding")
    public ResponseEntity<String> startOnboarding(@RequestHeader("Authorization") @NotBlank @NotNull String auth){
        String email = jwtTokenUtility.decodeJwtToken(auth);
        int driverId = driverService.getDriver(email).getId();
        return onboardingStateMachineService.markStartOnboarding(driverId);
    }

    @GetMapping("/auth/completed_training")
    public ResponseEntity<Driver> completedTraningEvent(@RequestHeader("Authorization") @NotBlank @NotNull String auth){
        String email = jwtTokenUtility.decodeJwtToken(auth);
        int driverId = driverService.getDriver(email).getId();
        return onboardingStateMachineService.passCompletedTrainingVideosEventToOSM(driverId);
    }

    @GetMapping("/auth/mark_ready")
    public ResponseEntity<Driver> markingReady(@RequestHeader("Authorization") @NotBlank @NotNull String auth){
        String email = jwtTokenUtility.decodeJwtToken(auth);
        int driverId = driverService.getDriver(email).getId();
        return onboardingStateMachineService.markingReady(driverId);
    }

    //This is for POC only. In real scenarios, this should be implmented by msg queue, updated on delivered events
    @GetMapping("/admin/TrackingDeviceDelivered")
    public ResponseEntity<Driver> delievredTrackingDevice(@RequestParam @Valid  int driverId) {
        return onboardingStateMachineService.passTrackingDeviceDeliveredEventToOSM(driverId);
    }







}
