package com.craft.onboarding.driveronboarding.repository;

import com.craft.onboarding.driveronboarding.model.Driver;
import com.craft.onboarding.driveronboarding.model.Vehicle;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VehicleRepository extends JpaRepository<Vehicle,Integer> {

    Vehicle findByVehicleNo(String vehicleNo);

    Vehicle findByDriverId(int driverId);
}