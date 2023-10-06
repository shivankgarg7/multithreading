package com.craft.onboarding.driveronboarding.service;

import com.craft.onboarding.driveronboarding.exception.DriverAlreadyExistsException;
import com.craft.onboarding.driveronboarding.exception.DriverNotFoundException;
import com.craft.onboarding.driveronboarding.exception.UsernameOrPasswordIsIncorrectException;
import com.craft.onboarding.driveronboarding.dto.AuthDTO;
import com.craft.onboarding.driveronboarding.model.Driver;
import com.craft.onboarding.driveronboarding.dto.DriverDTO;
import com.craft.onboarding.driveronboarding.repository.DriverRepository;
import com.craft.onboarding.driveronboarding.utils.JwtTokenUtility;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class DriverServiceImpl implements DriverService {

    @Autowired
    private JwtTokenUtility jwtTokenUtility;
    @Autowired
    private DriverRepository driverRepository;

    private PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();



    @Override
    public Driver signup(AuthDTO authDTO) {

        log.info("passwordEncoder="+passwordEncoder);
        // Check if a driver with the same email already exists
        Driver existingDriver =  driverRepository.findByEmail(authDTO.getEmail());
        if (existingDriver != null) {
            throw new DriverAlreadyExistsException("Email already registered");
        }

        String hashedPassword = passwordEncoder.encode(authDTO.getPassword());

        Driver newDriver = new Driver();
        newDriver.setEmail(authDTO.getEmail());
        newDriver.setPassword(hashedPassword);
        Driver signedupDriver = driverRepository.save(newDriver);

        return signedupDriver;
    }

    @Override
    public String login(AuthDTO authDTO) {
        Driver driver = driverRepository.findByEmail(authDTO.getEmail());
        if (driver == null || !passwordEncoder.matches(authDTO.getPassword(), driver.getPassword()) ) {
            throw new UsernameOrPasswordIsIncorrectException("Invalid email or password");
        }

        String token = jwtTokenUtility.generateJwtToken(driver);

        return token;
    }

    @Override
    public void deleteDriver(int id) {
        Driver driver = driverRepository.getReferenceById(id);
        driverRepository.delete(driver);
        System.out.println("Driver Has been Deleted ");
    }

    @Override
    public Driver getDriver(String email) {
        Driver driver = driverRepository.findByEmail(email);
        if(driver == null){
            throw new DriverNotFoundException("Driver Does not Exist in our system");
        }
        return driver;
    }

    @Override
    public Driver getDriver(int driverId) {
        Driver driver = driverRepository.findById(driverId);
        if(driver == null){
            throw new DriverNotFoundException("Driver Does not Exist in our system");
        }
        return driver;
    }


    @Override
    public int getDriverId(String email) {
        return getDriver(email).getId();
    }

    @Override
    public String getDriverCity(String email) {
        return getDriver(email).getCity();
    }


    @Override
    public void save(Driver driver){
        driverRepository.save(driver);
    }


    @Override
    public void addDriverDetails(DriverDTO driverDTO, String email){
        Driver driver = driverRepository.findByEmail(email);
        driver = convertToDriverEntity(driver, driverDTO);
        driverRepository.save(driver);
    }

    @Override
    public List getAllOnboardingDrivers(){
        return driverRepository.findAllByHasOnboardingStarted(true);
    }

    @Override
    public List<String> getCities(){
        //HardCoded for POC
        List<String> cities = new ArrayList<>();
        cities.add("Bengaluru");
        cities.add("Paris");
        return cities;
    }


    private Driver convertToDriverEntity(Driver driver, DriverDTO formData) {
        driver.setFirstName(formData.getFirstName());
        driver.setLastName(formData.getLastName());
        driver.setLicenseNumber(formData.getLicenseNumber());
        driver.setCity(formData.getCity());
        return driver;
    }


}
