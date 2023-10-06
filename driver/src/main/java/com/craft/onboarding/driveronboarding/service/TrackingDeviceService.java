package com.craft.onboarding.driveronboarding.service;

import com.craft.onboarding.driveronboarding.model.TrackingDevice;

import java.util.UUID;

public interface TrackingDeviceService {
    UUID  getTrackingDeviceId(int driverId);
    TrackingDevice getTrackingDevice(int driverId);
    void sendTrackingDevice(int driverId);

    void trackingDeviceDelivered(int driverId);
}
