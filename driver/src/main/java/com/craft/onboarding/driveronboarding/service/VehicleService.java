package com.craft.onboarding.driveronboarding.service;

import com.craft.onboarding.driveronboarding.enums.VehicleType;
import com.craft.onboarding.driveronboarding.model.Vehicle;
import com.craft.onboarding.driveronboarding.dto.VehicleDTO;

import java.util.List;


public interface VehicleService {
    public Vehicle addVehicleDetails(VehicleDTO vehicleDTO, String authorisationHeader);

    VehicleType getVehicleType(int driverId);

    List<VehicleType> getSupportedVehicleTypesList();
}
