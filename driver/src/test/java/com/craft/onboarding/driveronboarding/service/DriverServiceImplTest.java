package com.craft.onboarding.driveronboarding.service;

import com.craft.onboarding.driveronboarding.dto.AuthDTO;
import com.craft.onboarding.driveronboarding.dto.DriverDTO;
import com.craft.onboarding.driveronboarding.exception.DriverAlreadyExistsException;
import com.craft.onboarding.driveronboarding.exception.DriverNotFoundException;
import com.craft.onboarding.driveronboarding.exception.UsernameOrPasswordIsIncorrectException;
import com.craft.onboarding.driveronboarding.model.Driver;
import com.craft.onboarding.driveronboarding.repository.DriverRepository;
import com.craft.onboarding.driveronboarding.utils.JwtTokenUtility;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class DriverServiceImplTest {

    @InjectMocks
    private DriverServiceImpl driverService;

    @Mock
    private JwtTokenUtility jwtTokenUtility;

    @Mock
    private DriverRepository driverRepository;

    private PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testSignup_WhenDriverDoesNotExist_ShouldCreateDriver() {
        // Arrange
        AuthDTO authDTO = new AuthDTO();
        authDTO.setEmail("test@example.com");
        authDTO.setPassword("password");

        when(driverRepository.findByEmail(authDTO.getEmail())).thenReturn(null);
        when(driverRepository.save(any(Driver.class))).thenReturn(new Driver());

        // Act
        Driver signedupDriver = driverService.signup(authDTO);

        // Assert
        assertNotNull(signedupDriver);
        verify(driverRepository, times(1)).findByEmail(authDTO.getEmail());
        verify(driverRepository, times(1)).save(any(Driver.class));
    }

    @Test
    public void testSignup_WhenDriverExists_ShouldThrowException() {
        // Arrange
        AuthDTO authDTO = new AuthDTO();
        authDTO.setEmail("test@example.com");
        authDTO.setPassword("password");

        when(driverRepository.findByEmail(authDTO.getEmail())).thenReturn(new Driver());

        // Act & Assert
        assertThrows(DriverAlreadyExistsException.class, () -> driverService.signup(authDTO));
        verify(driverRepository, times(1)).findByEmail(authDTO.getEmail());
        verify(driverRepository, never()).save(any(Driver.class));
    }

    @Test
    public void testLogin_WhenDriverExistsAndPasswordMatches_ShouldReturnToken() {
        // Arrange
        AuthDTO authDTO = new AuthDTO();
        authDTO.setEmail("test@example.com");
        authDTO.setPassword("password");

        Driver driver = new Driver();
        driver.setEmail(authDTO.getEmail());
        driver.setPassword(passwordEncoder.encode(authDTO.getPassword()));

        when(driverRepository.findByEmail(authDTO.getEmail())).thenReturn(driver);
        when(jwtTokenUtility.generateJwtToken(driver)).thenReturn("token");

        // Act
        String token = driverService.login(authDTO);

        // Assert
        assertNotNull(token);
        verify(driverRepository, times(1)).findByEmail(authDTO.getEmail());
        verify(jwtTokenUtility, times(1)).generateJwtToken(driver);
    }

    @Test
    public void testLogin_WhenDriverNotFound_ShouldThrowException() {
        // Arrange
        AuthDTO authDTO = new AuthDTO();
        authDTO.setEmail("test@example.com");
        authDTO.setPassword("password");

        when(driverRepository.findByEmail(authDTO.getEmail())).thenReturn(null);

        // Act & Assert
        assertThrows(UsernameOrPasswordIsIncorrectException.class, () -> driverService.login(authDTO));
        verify(driverRepository, times(1)).findByEmail(authDTO.getEmail());
        verify(jwtTokenUtility, never()).generateJwtToken(any(Driver.class));
    }

    @Test
    public void testLogin_WhenPasswordDoesNotMatch_ShouldThrowException() {
        // Arrange
        AuthDTO authDTO = new AuthDTO();
        authDTO.setEmail("test@example.com");
        authDTO.setPassword("password");

        Driver driver = new Driver();
        driver.setEmail(authDTO.getEmail());
        driver.setPassword(passwordEncoder.encode("incorrect_password"));

        when(driverRepository.findByEmail(authDTO.getEmail())).thenReturn(driver);

        // Act & Assert
        assertThrows(UsernameOrPasswordIsIncorrectException.class, () -> driverService.login(authDTO));
        verify(driverRepository, times(1)).findByEmail(authDTO.getEmail());
        verify(jwtTokenUtility, never()).generateJwtToken(any(Driver.class));
    }

    @Test
    public void testDeleteDriver_WhenDriverExists_ShouldDeleteDriver() {
        // Arrange
        int driverId = 1;
        Driver driver = new Driver();
        driver.setId(driverId);

        when(driverRepository.getReferenceById(driverId)).thenReturn(driver);

        // Act
        driverService.deleteDriver(driverId);

        // Assert
        verify(driverRepository, times(1)).getReferenceById(driverId);
        verify(driverRepository, times(1)).delete(driver);
    }

    @Test
    public void testDeleteDriver_WhenDriverDoesNotExist_ShouldNotThrowException() {
        // Arrange
        int driverId = 1;

        when(driverRepository.getReferenceById(driverId)).thenReturn(null);

        // Act & Assert (no exception should be thrown)
        assertDoesNotThrow(() -> driverService.deleteDriver(driverId));
        verify(driverRepository, times(1)).getReferenceById(driverId);
        verify(driverRepository, never()).delete(any(Driver.class));
    }

    @Test
    public void testGetDriverByEmail_WhenDriverExists_ShouldReturnDriver() {
        // Arrange
        String email = "test@example.com";
        Driver driver = new Driver();
        driver.setEmail(email);

        when(driverRepository.findByEmail(email)).thenReturn(driver);

        // Act
        Driver resultDriver = driverService.getDriver(email);

        // Assert
        assertNotNull(resultDriver);
        assertEquals(email, resultDriver.getEmail());
        verify(driverRepository, times(1)).findByEmail(email);
    }

    @Test
    public void testGetDriverByEmail_WhenDriverDoesNotExist_ShouldThrowException() {
        // Arrange
        String email = "nonexistent@example.com";

        when(driverRepository.findByEmail(email)).thenReturn(null);

        // Act & Assert
        assertThrows(DriverNotFoundException.class, () -> driverService.getDriver(email));
        verify(driverRepository, times(1)).findByEmail(email);
    }

    @Test
    public void testGetDriverById_WhenDriverExists_ShouldReturnDriver() {
        // Arrange
        int driverId = 1;
        Driver driver = new Driver();
        driver.setId(driverId);

        when(driverRepository.findById(driverId)).thenReturn(driver);

        // Act
        Driver resultDriver = driverService.getDriver(driverId);

        // Assert
        assertNotNull(resultDriver);
        assertEquals(driverId, resultDriver.getId());
        verify(driverRepository, times(1)).findById(driverId);
    }

    @Test
    public void testGetDriverById_WhenDriverDoesNotExist_ShouldThrowException() {
        // Arrange
        int driverId = 999; // Assuming driver ID does not exist

        when(driverRepository.findById(driverId)).thenReturn(null);

        // Act & Assert
        assertThrows(DriverNotFoundException.class, () -> driverService.getDriver(driverId));
        verify(driverRepository, times(1)).findById(driverId);
    }

    @Test
    public void testGetDriverId_WhenDriverExists_ShouldReturnDriverId() {
        // Arrange
        String email = "test@example.com";
        int expectedDriverId = 1;
        Driver driver = new Driver();
        driver.setId(expectedDriverId);

        when(driverRepository.findByEmail(email)).thenReturn(driver);

        // Act
        int driverId = driverService.getDriverId(email);

        // Assert
        assertEquals(expectedDriverId, driverId);
        verify(driverRepository, times(1)).findByEmail(email);
    }

    @Test
    public void testGetDriverCity_WhenDriverExists_ShouldReturnDriverCity() {
        // Arrange
        String email = "test@example.com";
        String expectedCity = "New York";
        Driver driver = new Driver();
        driver.setCity(expectedCity);

        when(driverRepository.findByEmail(email)).thenReturn(driver);

        // Act
        String driverCity = driverService.getDriverCity(email);

        // Assert
        assertEquals(expectedCity, driverCity);
        verify(driverRepository, times(1)).findByEmail(email);
    }

    @Test
    public void testSave_ShouldCallRepositorySaveMethod() {
        // Arrange
        Driver driver = new Driver();

        // Act
        driverService.save(driver);

        // Assert
        verify(driverRepository, times(1)).save(driver);
    }

    @Test
    public void testAddDriverDetails_ShouldUpdateDriverEntityAndCallRepositorySaveMethod() {
        // Arrange
        String email = "test@example.com";
        DriverDTO driverDTO = new DriverDTO();
        driverDTO.setFirstName("John");
        driverDTO.setLastName("Doe");

        Driver existingDriver = new Driver();
        existingDriver.setEmail(email);

        when(driverRepository.findByEmail(email)).thenReturn(existingDriver);
        when(driverRepository.save(existingDriver)).thenReturn(existingDriver);

        // Act
        driverService.addDriverDetails(driverDTO, email);

        // Assert
        verify(driverRepository, times(1)).findByEmail(email);
        verify(driverRepository, times(1)).save(existingDriver);
        assertEquals("John", existingDriver.getFirstName());
        assertEquals("Doe", existingDriver.getLastName());
    }

    @Test
    public void testGetAllOnboardingDrivers_ShouldReturnListOfDrivers() {
        // Arrange
        List<Driver> onboardingDrivers = new ArrayList<>();
        when(driverRepository.findAllByHasOnboardingStarted(true)).thenReturn(onboardingDrivers);

        // Act
        List result = driverService.getAllOnboardingDrivers();

        // Assert
        assertEquals(onboardingDrivers, result);
        verify(driverRepository, times(1)).findAllByHasOnboardingStarted(true);
    }

    @Test
    public void testGetCities_ShouldReturnListOfCities() {
        // Arrange
        List<String> expectedCities = new ArrayList<>();
        expectedCities.add("Bengaluru");
        expectedCities.add("Paris");

        // Act
        List<String> cities = driverService.getCities();

        // Assert
        assertEquals(expectedCities, cities);
    }

    // Add more test cases for other methods in the DriverServiceImpl class
}
