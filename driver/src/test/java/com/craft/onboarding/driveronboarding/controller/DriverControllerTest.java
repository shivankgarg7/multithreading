package com.craft.onboarding.driveronboarding.controller;

import com.craft.onboarding.driveronboarding.dto.DriverDTO;
import com.craft.onboarding.driveronboarding.dto.VehicleDTO;
import com.craft.onboarding.driveronboarding.model.Vehicle;
import com.craft.onboarding.driveronboarding.service.DriverService;
import com.craft.onboarding.driveronboarding.service.VehicleService;
import com.craft.onboarding.driveronboarding.utils.JwtTokenUtility;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

public class DriverControllerTest {

    @InjectMocks
    private DriverController driverController;

    @Mock
    private DriverService driverService;

    @Mock
    private VehicleService vehicleService;

    @Mock
    private JwtTokenUtility jwtTokenUtility;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testShowDriverDetailsForm_ShouldReturnDriverDetailsForm() {
        // Arrange
        DriverDTO driverDTO = new DriverDTO();
        BindingResult bindingResult = mock(BindingResult.class);
        Model model = mock(Model.class);

        // Act
        String viewName = driverController.showDriverDetailsForm(driverDTO, bindingResult, model);

        // Assert
        assertEquals("driver-details-for", viewName);

    }

    @Test
    public void testSaveDriverDetailsForm_ShouldReturnRedirect() {
        // Arrange
        DriverDTO driverDTO = new DriverDTO();
        BindingResult bindingResult = mock(BindingResult.class);
        String authorizationHeader = "sample-token";
        Model model = mock(Model.class);

        // Mocking
        when(jwtTokenUtility.decodeJwtToken(authorizationHeader)).thenReturn("sample-email");

        // Act
        String viewName = driverController.saveDriverDetailsForm(driverDTO, bindingResult, authorizationHeader, model);

        // Assert
        assertEquals("redirect:/auth/vehicle/form", viewName);
        verify(jwtTokenUtility, times(1)).decodeJwtToken(authorizationHeader);
        verify(driverService, times(1)).addDriverDetails(driverDTO, "sample-email");
    }

    @Test
    public void testAddVehicleDetails_ShouldReturnVehicleForm() {
        // Arrange
        VehicleDTO vehicleDTO = new VehicleDTO();
        BindingResult bindingResult = mock(BindingResult.class);
        Model model = mock(Model.class);

        // Act
        String viewName = driverController.addVehicleDetails(vehicleDTO, bindingResult, model);

        // Assert
        assertEquals("VehicleForm", viewName);

    }

    @Test
    public void testSaveVehicleDetails_ShouldReturnOkResponseEntity() {
        // Arrange
        VehicleDTO vehicleDTO = new VehicleDTO();
        BindingResult bindingResult = mock(BindingResult.class);
        String authorizationHeader = "sample-token";
        Model model = mock(Model.class);

        // Mocking
        when(jwtTokenUtility.decodeJwtToken(authorizationHeader)).thenReturn("sample-email");

        // Act
        ResponseEntity<Vehicle> response = driverController.saveVehicleDetails(vehicleDTO, bindingResult, authorizationHeader, model);

        // Assert
        assertNotNull(response);
        assertEquals("Vehicle info saved", response.getBody());
        assertEquals(200, response.getStatusCodeValue());
        verify(jwtTokenUtility, times(1)).decodeJwtToken(authorizationHeader);
        verify(vehicleService, times(1)).addVehicleDetails(vehicleDTO, "sample-email");
    }

    @Test
    public void testDeleteDriver_ShouldInvokeServiceMethod() {
        // Arrange
        int id = 1;

        // Act
        driverController.deleteDriver(id);

        // Assert
        verify(driverService, times(1)).deleteDriver(id);
    }
}
