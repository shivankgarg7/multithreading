package com.craft.onboarding.driveronboarding.service;

import com.craft.onboarding.driveronboarding.model.TrackingDevice;
import com.craft.onboarding.driveronboarding.repository.TrackingDeviceRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@Slf4j
public class TrackingDeviceServiceImpl implements TrackingDeviceService{

    @Autowired
    TrackingDeviceRepository trackingDeviceRepository;

    @Autowired
    DriverService driverService;

    @Override
    public UUID getTrackingDeviceId(int driverId) {
       return trackingDeviceRepository.findByDriverId(driverId).getId();
    }

    @Override
    public TrackingDevice getTrackingDevice(int driverId) {
        return trackingDeviceRepository.findByDriverId(driverId);
    }

    @Override
    public void sendTrackingDevice(int driverId) {
        TrackingDevice trackingDevice  =  new TrackingDevice();
        trackingDevice.setDriver(driverService.getDriver(driverId));
        trackingDeviceRepository.save(trackingDevice);
    }

    @Override
    public void trackingDeviceDelivered(int driverId){
        TrackingDevice trackingDevice  = trackingDeviceRepository.findByDriverId(driverId);
        trackingDevice.setDelivered(true);
        trackingDeviceRepository.save(trackingDevice);
    }

}
