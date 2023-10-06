package com.craft.onboarding.driveronboarding.service;

import com.craft.onboarding.driveronboarding.dto.AuthDTO;
import com.craft.onboarding.driveronboarding.model.Driver;
import com.craft.onboarding.driveronboarding.dto.DriverDTO;

import java.util.List;


public interface DriverService {

    public Driver signup(AuthDTO authDTO);

    public String login(AuthDTO authDTO);
    public void deleteDriver(int driver);

    public Driver getDriver(String email);
    
    public Driver getDriver(int driverId);

    public int getDriverId(String email);

    public String getDriverCity(String email);

    public void addDriverDetails(DriverDTO driverDTO, String email);

    public List getAllOnboardingDrivers();

    void save(Driver driver);

    List<String> getCities();
}
