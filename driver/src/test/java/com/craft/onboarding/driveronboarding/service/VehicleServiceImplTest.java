package com.craft.onboarding.driveronboarding.service;

import com.craft.onboarding.driveronboarding.dto.VehicleDTO;
import com.craft.onboarding.driveronboarding.enums.VehicleType;
import com.craft.onboarding.driveronboarding.model.Driver;
import com.craft.onboarding.driveronboarding.model.Vehicle;
import com.craft.onboarding.driveronboarding.repository.VehicleRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

public class VehicleServiceImplTest {

    @InjectMocks
    private VehicleServiceImpl vehicleService;

    @Mock
    private DriverService driverService;

    @Mock
    private VehicleRepository vehicleRepository;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testAddVehicleDetails_ShouldAddVehicle() {
        // Arrange
        String authorisationHeader = "test@example.com";
        VehicleDTO vehicleDTO = createTestVehicleDTO();

        Driver driver = new Driver();
        driver.setEmail(authorisationHeader);

        when(driverService.getDriver(authorisationHeader)).thenReturn(driver);
        when(vehicleRepository.save(any(Vehicle.class))).thenReturn(new Vehicle());

        // Act
        Vehicle addedVehicle = vehicleService.addVehicleDetails(vehicleDTO, authorisationHeader);

        // Assert
        assertNotNull(addedVehicle);
        verify(driverService, times(1)).getDriver(authorisationHeader);
        verify(vehicleRepository, times(1)).save(any(Vehicle.class));
    }

    @Test
    public void testGetVehicleType_WhenDriverExists_ShouldReturnVehicleType() {
        // Arrange
        int driverId = 1;
        VehicleType expectedVehicleType = VehicleType.SEDAN;

        Vehicle vehicle = new Vehicle();
        vehicle.setVehicleType(expectedVehicleType);

        when(vehicleRepository.findByDriverId(driverId)).thenReturn(vehicle);

        // Act
        VehicleType resultVehicleType = vehicleService.getVehicleType(driverId);

        // Assert
        assertEquals(expectedVehicleType, resultVehicleType);
        verify(vehicleRepository, times(1)).findByDriverId(driverId);
    }

    @Test
    public void testGetSupportedVehicleTypesList_ShouldReturnListOfVehicleTypes() {
        // Arrange
        List<VehicleType> expectedVehicleTypes = new ArrayList<>();
        expectedVehicleTypes.add(VehicleType.SUV);
        expectedVehicleTypes.add(VehicleType.SEDAN);

        // Act
        List<VehicleType> vehicleTypes = vehicleService.getSupportedVehicleTypesList();

        // Assert
        assertEquals(expectedVehicleTypes, vehicleTypes);
    }

    // Helper method to create a test VehicleDTO
    private VehicleDTO createTestVehicleDTO() {
        VehicleDTO vehicleDTO = new VehicleDTO();
        vehicleDTO.setVehicleNo("ABC123");
        vehicleDTO.setVehicleType(VehicleType.SEDAN);
        vehicleDTO.setVehicleRegistraionCity("New York");
        vehicleDTO.setBrand("Toyota");
        vehicleDTO.setModelNo("Camry");
        return vehicleDTO;
    }
}
