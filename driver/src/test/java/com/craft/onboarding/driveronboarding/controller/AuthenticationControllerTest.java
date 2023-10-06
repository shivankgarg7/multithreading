package com.craft.onboarding.driveronboarding.controller;

import com.craft.onboarding.driveronboarding.dto.AuthDTO;
import com.craft.onboarding.driveronboarding.model.Driver;
import com.craft.onboarding.driveronboarding.service.DriverService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

public class AuthenticationControllerTest {

    @InjectMocks
    private AuthenticationController authenticationController;

    @Mock
    private DriverService driverService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testRegisterDriver_ShouldReturnCreated() {
        // Arrange
        AuthDTO authDTO = new AuthDTO();
        Driver driver = new Driver();
        when(driverService.signup(authDTO)).thenReturn(driver);

        // Act
        ResponseEntity<Driver> response = authenticationController.registerDriver(authDTO);

        // Assert
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
        verify(driverService, times(1)).signup(authDTO);
    }

    @Test
    public void testLoginDriver_ShouldReturnOk() {
        // Arrange
        AuthDTO authDTO = new AuthDTO();
        String token = "sample-token";
        when(driverService.login(authDTO)).thenReturn(token);

        // Act
        ResponseEntity<String> response = authenticationController.loginDriver(authDTO);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(token, response.getBody());
        verify(driverService, times(1)).login(authDTO);
    }
}
