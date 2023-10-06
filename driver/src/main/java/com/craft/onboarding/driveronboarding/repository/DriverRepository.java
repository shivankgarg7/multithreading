package com.craft.onboarding.driveronboarding.repository;

import com.craft.onboarding.driveronboarding.model.Driver;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DriverRepository extends JpaRepository<Driver,Integer> {

    Driver findByEmail(String email);

    Driver findById(int driverId);

    List<Driver> findAllByHasOnboardingStarted(boolean hasOnboardingStarted);

}
