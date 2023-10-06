package com.craft.onboarding.driveronboarding.repository;

import com.craft.onboarding.driveronboarding.model.TrackingDevice;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface TrackingDeviceRepository extends JpaRepository<TrackingDevice, UUID> {
    TrackingDevice findByDriverId(int driverId);

}