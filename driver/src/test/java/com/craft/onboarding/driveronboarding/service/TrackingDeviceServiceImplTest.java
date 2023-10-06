package com.craft.onboarding.driveronboarding.service;

import com.craft.onboarding.driveronboarding.model.Driver;
import com.craft.onboarding.driveronboarding.model.TrackingDevice;
import com.craft.onboarding.driveronboarding.repository.TrackingDeviceRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class TrackingDeviceServiceImplTest {

    @InjectMocks
    private TrackingDeviceServiceImpl trackingDeviceService;

    @Mock
    private TrackingDeviceRepository trackingDeviceRepository;

    @Mock
    private DriverService driverService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testGetTrackingDeviceId_WhenDriverExists_ShouldReturnTrackingDeviceId() {
        // Arrange
        int driverId = 1;
        UUID expectedTrackingDeviceId = UUID.randomUUID();

        TrackingDevice trackingDevice = new TrackingDevice();
        trackingDevice.setId(expectedTrackingDeviceId);

        when(trackingDeviceRepository.findByDriverId(driverId)).thenReturn(trackingDevice);

        // Act
        UUID trackingDeviceId = trackingDeviceService.getTrackingDeviceId(driverId);

        // Assert
        assertEquals(expectedTrackingDeviceId, trackingDeviceId);
        verify(trackingDeviceRepository, times(1)).findByDriverId(driverId);
    }

    @Test
    public void testGetTrackingDevice_WhenDriverExists_ShouldReturnTrackingDevice() {
        // Arrange
        int driverId = 1;
        TrackingDevice expectedTrackingDevice = new TrackingDevice();

        when(trackingDeviceRepository.findByDriverId(driverId)).thenReturn(expectedTrackingDevice);

        // Act
        TrackingDevice trackingDevice = trackingDeviceService.getTrackingDevice(driverId);

        // Assert
        assertEquals(expectedTrackingDevice, trackingDevice);
        verify(trackingDeviceRepository, times(1)).findByDriverId(driverId);
    }

    @Test
    public void testSendTrackingDevice_ShouldCreateAndSaveTrackingDevice() {
        // Arrange
        int driverId = 1;
        Driver driver = new Driver();

        when(driverService.getDriver(driverId)).thenReturn(driver);

        // Act
        trackingDeviceService.sendTrackingDevice(driverId);

        // Assert
        verify(driverService, times(1)).getDriver(driverId);
        verify(trackingDeviceRepository, times(1)).save(any(TrackingDevice.class));
    }

    @Test
    public void testTrackingDeviceDelivered_ShouldUpdateTrackingDeviceDeliveredStatus() {
        // Arrange
        int driverId = 1;
        TrackingDevice trackingDevice = new TrackingDevice();

        when(trackingDeviceRepository.findByDriverId(driverId)).thenReturn(trackingDevice);

        // Act
        trackingDeviceService.trackingDeviceDelivered(driverId);

        // Assert
        assertTrue(trackingDevice.isDelivered());
        verify(trackingDeviceRepository, times(1)).findByDriverId(driverId);
        verify(trackingDeviceRepository, times(1)).save(trackingDevice);
    }
}
