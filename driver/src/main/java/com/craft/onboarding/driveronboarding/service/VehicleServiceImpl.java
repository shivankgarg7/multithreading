package com.craft.onboarding.driveronboarding.service;

import com.craft.onboarding.driveronboarding.enums.VehicleType;
import com.craft.onboarding.driveronboarding.model.Vehicle;
import com.craft.onboarding.driveronboarding.dto.VehicleDTO;
import com.craft.onboarding.driveronboarding.repository.VehicleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class VehicleServiceImpl implements VehicleService{

    @Autowired
    DriverService driverService;

    @Autowired
    VehicleRepository vehicleRepository;
    @Override
    public Vehicle addVehicleDetails(VehicleDTO vehicleDTO, String authorisationHeader) {
        Vehicle vehicle  = new Vehicle();
        vehicle.setDriver(driverService.getDriver(authorisationHeader));
        vehicle = convertToVehicleEntity(vehicle, vehicleDTO);
        return vehicleRepository.save(vehicle);
    }

    @Override
    public VehicleType getVehicleType(int driverId) {
        return vehicleRepository.findByDriverId(driverId).getVehicleType();
    }


    private Vehicle convertToVehicleEntity(Vehicle vehicle, VehicleDTO vehicleDTO) {
        vehicle.setVehicleNo(vehicleDTO.getVehicleNo());
        vehicle.setVehicleType(vehicleDTO.getVehicleType());
        vehicle.setVehicleRegistraionCity(vehicleDTO.getVehicleRegistraionCity());
        vehicle.setBrand(vehicleDTO.getBrand());
        vehicle.setModelNo(vehicleDTO.getModelNo());
        return vehicle;
    }

    @Override
    public List<VehicleType> getSupportedVehicleTypesList(){
        List<VehicleType> vehicleTypes = new ArrayList<>();
        vehicleTypes.add(VehicleType.SUV);
        vehicleTypes.add(VehicleType.SEDAN);
        return vehicleTypes;
    }


}
