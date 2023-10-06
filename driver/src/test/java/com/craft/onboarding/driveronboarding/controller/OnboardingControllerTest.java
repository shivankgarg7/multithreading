package com.craft.onboarding.driveronboarding.controller;

import com.craft.onboarding.driveronboarding.model.Driver;
import com.craft.onboarding.driveronboarding.service.DriverService;
import com.craft.onboarding.driveronboarding.service.OnboardingSMService;
import com.craft.onboarding.driveronboarding.utils.JwtTokenUtility;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

public class OnboardingControllerTest {

    @InjectMocks
    private OnboardingController onboardingController;

    @Mock
    private OnboardingSMService onboardingSMService;

    @Mock
    private JwtTokenUtility jwtTokenUtility;

    @Mock
    DriverService driverService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testGetDriver_ShouldReturnDriver() {
        // Arrange
        int driverId = 1;
        Driver driver = new Driver(); // Create a Driver object
        when(onboardingSMService.getDriver(driverId)).thenReturn(driver);

        // Act
        ResponseEntity<Driver> response = onboardingController.getDriver(driverId);

        // Assert
        assertEquals(ResponseEntity.ok(driver), response);
    }

    @Test
    public void testApproveEvent_ShouldReturnDriver() {
        // Arrange
        int driverId = 1;
        Driver driver = new Driver(); // Create a Driver object
        when(onboardingSMService.passApprovedEventToOSM(driverId)).thenReturn(ResponseEntity.ok(driver));

        // Act
        ResponseEntity<Driver> response = onboardingController.approveEvent(driverId);

        // Assert
        assertEquals(ResponseEntity.ok(driver), response);
    }

    @Test
    public void testStartOnboarding_ShouldReturnString() {
        // Arrange
        String authorizationHeader = "Bearer token";
        String email = "test@example.com";
        Driver driver = new Driver();
        driver.setId(1);
        int driverId = 1;
        when(jwtTokenUtility.decodeJwtToken(authorizationHeader)).thenReturn(email);
        when(driverService.getDriver(email)).thenReturn(driver); // Create a Driver object
        when(onboardingSMService.markStartOnboarding(driverId)).thenReturn(ResponseEntity.ok("Onboarding_started"));

        // Act
        ResponseEntity<String> response = onboardingController.startOnboarding(authorizationHeader);

        // Assert
        assertEquals(ResponseEntity.ok("Onboarding_started"), response);
    }

    @Test
    public void testCompletedTrainingEvent_ShouldReturnDriver() {
        // Arrange
        String authorizationHeader = "Bearer token";
        String email = "test@example.com";
        int driverId = 1;
        Driver driver = new Driver();
        driver.setId(1);
        when(jwtTokenUtility.decodeJwtToken(authorizationHeader)).thenReturn(email);
        when(driverService.getDriver(email)).thenReturn(driver); // Create a Driver object
        when(onboardingSMService.passCompletedTrainingVideosEventToOSM(driverId)).thenReturn(ResponseEntity.ok(driver));

        // Act
        ResponseEntity<Driver> response = onboardingController.completedTraningEvent(authorizationHeader);

        // Assert
        assertEquals(ResponseEntity.ok(driver), response);
    }

    @Test
    public void testMarkingReady_ShouldReturnDriver() {
        // Arrange
        String authorizationHeader = "Bearer token";
        String email = "test@example.com";
        int driverId = 1;
        Driver driver = new Driver();
        driver.setId(1);
        when(jwtTokenUtility.decodeJwtToken(authorizationHeader)).thenReturn(email);
        when(driverService.getDriver(email)).thenReturn(driver); // Create a Driver object
        when(onboardingSMService.markingReady(driverId)).thenReturn(ResponseEntity.ok(driver));

        // Act
        ResponseEntity<Driver> response = onboardingController.markingReady(authorizationHeader);

        // Assert
        assertEquals(ResponseEntity.ok(driver), response);
    }

    @Test
    public void testDeliveredTrackingDevice_ShouldReturnDriver() {
        // Arrange
        int driverId = 1;
        Driver driver = new Driver(); // Create a Driver object
        when(onboardingSMService.passTrackingDeviceDeliveredEventToOSM(driverId)).thenReturn(ResponseEntity.ok(driver));

        // Act
        ResponseEntity<Driver> response = onboardingController.delievredTrackingDevice(driverId);

        // Assert
        assertEquals(ResponseEntity.ok(driver), response);
    }

    // Add more test methods for other endpoints in the OnboardingController class
}
