package com.craft.onboarding.driveronboarding.service;

import com.craft.onboarding.driveronboarding.OnboardingStateMachine.StateMachineConfigurer;
import com.craft.onboarding.driveronboarding.enums.VehicleType;
import com.craft.onboarding.driveronboarding.model.Driver;
import com.craft.onboarding.driveronboarding.model.TrackingDevice;
import com.craft.onboarding.driveronboarding.repository.TrackingDeviceRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.Message;
import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.data.redis.RedisStateMachinePersister;
import org.springframework.statemachine.state.State;
import reactor.core.Disposable;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class OnboardingSMServiceImplTest {

    @InjectMocks
    private OnboardingSMServiceImpl onboardingSMService;

    @Mock
    Disposable disposable;

    @Mock
    Flux flux;

    @Mock
    private DriverService driverService;

    @Mock
    StateMachine sm;

    @Mock
    Mono<Void> mv;

    @Mock
    State state;

    @Mock
    TrackingDeviceRepository trackingDeviceRepository;

    @Mock
    private StateMachineConfigurer stateMachineConfigurer;

    @Mock
    private VehicleService vehicleService;

    @Mock
    private RedisStateMachinePersister redisStateMachinePersister;

    @Mock
    Void v;

    @Mock
    private TrackingDeviceService trackingDeviceService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testMarkStartOnboarding_ShouldReturnOnboardingStarted() throws Exception {
        // Arrange
        int driverId = 1;

        // Mocking


        Driver driver = new Driver();
        TrackingDevice trackingDevice = new TrackingDevice();
        trackingDevice.setDelivered(true);
        when(driverService.getDriver(driverId)).thenReturn(driver);
        when(trackingDeviceService.getTrackingDevice(driverId)).thenReturn(trackingDevice);
        when(stateMachineConfigurer.createStateMachine(anyString(), any())).thenReturn(mock(StateMachine.class));

        String s = "CAN_MARK_READY_STATE";
        driver.setCity("a");
        trackingDevice.setDelivered(false);
        //when(driver.getCity()).thenReturn("a");
        when(vehicleService.getVehicleType(driverId)).thenReturn(VehicleType.SUV);
        when(driverService.getDriver(driverId)).thenReturn(driver);
        when(trackingDeviceService.getTrackingDevice(driverId)).thenReturn(trackingDevice);
        when(stateMachineConfigurer.createStateMachine(anyString(), any())).thenReturn(sm);
        when(sm.getState()).thenReturn(state);
        when(state.getId()).thenReturn(s);
        //when(s.compareTo(Constants.CAN_MARK_READY_STATE)).thenReturn(0);

        when(sm.sendEvent((Mono<Message>) any()) ).thenReturn(flux);

        when(flux.subscribe()).thenReturn(disposable);
        when(sm.startReactively()).thenReturn(mv);
        when(mv.block()).thenReturn(v);
        // Act
        ResponseEntity<String> response = onboardingSMService.markStartOnboarding(driverId);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Onboarding_started", response.getBody());
        verify(driverService, times(1)).getDriver(driverId);
        verify(stateMachineConfigurer, times(1)).createStateMachine(anyString(), any());
        verify(redisStateMachinePersister, times(0)).restore(sm, "driver:1");
        verify(redisStateMachinePersister, times(1)).persist(sm, "driver:1");
    }

    @Test
    public void testPassApprovedEventToOSM_ShouldReturnDriver() throws Exception {
        // Arrange
        int driverId = 1;

        // Mocking


        Driver driver = new Driver();
        TrackingDevice trackingDevice = new TrackingDevice();
        trackingDevice.setDelivered(true);
        when(driverService.getDriver(driverId)).thenReturn(driver);
        when(trackingDeviceService.getTrackingDevice(driverId)).thenReturn(trackingDevice);
        when(stateMachineConfigurer.createStateMachine(anyString(), any())).thenReturn(mock(StateMachine.class));

        String s = "CAN_MARK_READY_STATE";
        driver.setCity("a");
        trackingDevice.setDelivered(false);
        //when(driver.getCity()).thenReturn("a");
        when(vehicleService.getVehicleType(driverId)).thenReturn(VehicleType.SUV);
        when(driverService.getDriver(driverId)).thenReturn(driver);
        when(trackingDeviceService.getTrackingDevice(driverId)).thenReturn(trackingDevice);
        when(stateMachineConfigurer.createStateMachine(anyString(), any())).thenReturn(sm);
        when(sm.getState()).thenReturn(state);
        when(state.getId()).thenReturn(s);
        //when(s.compareTo(Constants.CAN_MARK_READY_STATE)).thenReturn(0);

        when(sm.sendEvent((Mono<Message>) any()) ).thenReturn(flux);

        when(flux.subscribe()).thenReturn(disposable);
        when(sm.startReactively()).thenReturn(mv);
        when(mv.block()).thenReturn(v);
        // Act
        ResponseEntity<Driver> response = onboardingSMService.passApprovedEventToOSM(driverId);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        verify(driverService, times(1)).getDriver(driverId);
        verify(stateMachineConfigurer, times(1)).createStateMachine(anyString(), any());
        verify(redisStateMachinePersister, times(1)).restore(any(), anyString());
        verify(redisStateMachinePersister, times(1)).persist(any(), anyString());
    }

    @Test
    public void testPassCompletedTrainingVideosEventToOSM_ShouldReturnDriver() throws Exception {
        // Arrange
        int driverId = 1;

        // Mocking


        Driver driver = new Driver();
        TrackingDevice trackingDevice = new TrackingDevice();
        trackingDevice.setDelivered(true);
        when(driverService.getDriver(driverId)).thenReturn(driver);
        when(trackingDeviceService.getTrackingDevice(driverId)).thenReturn(trackingDevice);
        when(stateMachineConfigurer.createStateMachine(anyString(), any())).thenReturn(mock(StateMachine.class));

        String s = "CAN_MARK_READY_STATE";
        driver.setCity("a");
        trackingDevice.setDelivered(false);
        //when(driver.getCity()).thenReturn("a");
        when(vehicleService.getVehicleType(driverId)).thenReturn(VehicleType.SUV);
        when(driverService.getDriver(driverId)).thenReturn(driver);
        when(trackingDeviceService.getTrackingDevice(driverId)).thenReturn(trackingDevice);
        when(stateMachineConfigurer.createStateMachine(anyString(), any())).thenReturn(sm);
        when(sm.getState()).thenReturn(state);
        when(state.getId()).thenReturn(s);
        //when(s.compareTo(Constants.CAN_MARK_READY_STATE)).thenReturn(0);

        when(sm.sendEvent((Mono<Message>) any()) ).thenReturn(flux);

        when(flux.subscribe()).thenReturn(disposable);
        when(sm.startReactively()).thenReturn(mv);
        when(mv.block()).thenReturn(v);
        // Act
        ResponseEntity<Driver> response = onboardingSMService.passCompletedTrainingVideosEventToOSM(driverId);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        verify(driverService, times(1)).getDriver(driverId);
        verify(stateMachineConfigurer, times(1)).createStateMachine(anyString(), any());
        verify(redisStateMachinePersister, times(1)).restore(any(), anyString());
        verify(redisStateMachinePersister, times(1)).persist(any(), anyString());
    }

    @Test
    public void testPassTrackingDeviceDeliveredEventToOSM_ShouldReturnDriver() throws Exception {
        // Arrange
        int driverId = 1;
        UUID trackingDeviceId = UUID.randomUUID();

        // Mocking


        Driver driver = new Driver();
        TrackingDevice trackingDevice = new TrackingDevice();
        trackingDevice.setDelivered(true);
        when(driverService.getDriver(driverId)).thenReturn(driver);
        when(trackingDeviceService.getTrackingDevice(driverId)).thenReturn(trackingDevice);
        when(stateMachineConfigurer.createStateMachine(anyString(), any())).thenReturn(mock(StateMachine.class));

        String s = "CAN_MARK_READY_STATE";
        driver.setCity("a");
        trackingDevice.setDelivered(false);
        //when(driver.getCity()).thenReturn("a");
        when(vehicleService.getVehicleType(driverId)).thenReturn(VehicleType.SUV);
        when(driverService.getDriver(driverId)).thenReturn(driver);
        when(trackingDeviceService.getTrackingDevice(driverId)).thenReturn(trackingDevice);
        when(stateMachineConfigurer.createStateMachine(anyString(), any())).thenReturn(sm);
        when(sm.getState()).thenReturn(state);
        when(state.getId()).thenReturn(s);
        //when(s.compareTo(Constants.CAN_MARK_READY_STATE)).thenReturn(0);

        when(sm.sendEvent((Mono<Message>) any()) ).thenReturn(flux);

        when(flux.subscribe()).thenReturn(disposable);
        when(sm.startReactively()).thenReturn(mv);
        when(mv.block()).thenReturn(v);

        // Act
        ResponseEntity<Driver> response = onboardingSMService.passTrackingDeviceDeliveredEventToOSM(driverId);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        verify(driverService, times(1)).getDriver(driverId);
        verify(trackingDeviceService, times(0)).getTrackingDeviceId(driverId);
        verify(stateMachineConfigurer, times(1)).createStateMachine(anyString(), any());
        verify(redisStateMachinePersister, times(1)).restore(any(), anyString());
        verify(redisStateMachinePersister, times(1)).persist(any(), anyString());
    }

    @Test
    public void testPassTrackingDeviceDeliveredEventToOSM_ShouldUpdateTrackingDevice() throws Exception {
        // Arrange
        int driverId = 1;
        UUID trackingDeviceId = UUID.randomUUID();

        // Mocking


        Driver driver = new Driver();
        TrackingDevice trackingDevice = new TrackingDevice();
        trackingDevice.setDelivered(true);
        when(driverService.getDriver(driverId)).thenReturn(driver);
        when(trackingDeviceService.getTrackingDevice(driverId)).thenReturn(trackingDevice);
        when(stateMachineConfigurer.createStateMachine(anyString(), any())).thenReturn(mock(StateMachine.class));

        String s = "CAN_MARK_READY_STATE";
        driver.setCity("a");
        trackingDevice.setDelivered(false);
        //when(driver.getCity()).thenReturn("a");
        when(vehicleService.getVehicleType(driverId)).thenReturn(VehicleType.SUV);
        when(driverService.getDriver(driverId)).thenReturn(driver);
        when(trackingDeviceService.getTrackingDevice(driverId)).thenReturn(trackingDevice);
        when(stateMachineConfigurer.createStateMachine(anyString(), any())).thenReturn(sm);
        when(sm.getState()).thenReturn(state);
        when(state.getId()).thenReturn(s);
        when(trackingDeviceRepository.findByDriverId(driverId)).thenReturn(trackingDevice);
        when(trackingDeviceRepository.save(trackingDevice)).thenReturn(trackingDevice);
        //when(s.compareTo(Constants.CAN_MARK_READY_STATE)).thenReturn(0);

        trackingDevice.setDelivered(true);
        when(sm.sendEvent((Mono<Message>) any()) ).thenReturn(flux);

        when(flux.subscribe()).thenReturn(disposable);
        when(sm.startReactively()).thenReturn(mv);
        when(mv.block()).thenReturn(v);

        // Act
        onboardingSMService.passTrackingDeviceDeliveredEventToOSM(driverId);

        // Assert
        assertTrue(trackingDevice.isDelivered());
        verify(trackingDeviceService, times(1)).trackingDeviceDelivered(driverId);
    }

    @Test
    public void testMarkingReady_WhenDriverCanMarkReady_ShouldReturnDriver() throws Exception {
        // Arrange
        int driverId = 1;

        // Mocking


        Driver driver = new Driver();
        TrackingDevice trackingDevice = new TrackingDevice();
        trackingDevice.setDelivered(true);
        when(driverService.getDriver(driverId)).thenReturn(driver);
        when(trackingDeviceService.getTrackingDevice(driverId)).thenReturn(trackingDevice);
        when(stateMachineConfigurer.createStateMachine(anyString(), any())).thenReturn(mock(StateMachine.class));

        String s = "CAN_MARK_READY_STATE";
        driver.setCity("a");
        trackingDevice.setDelivered(false);
        //when(driver.getCity()).thenReturn("a");
        when(vehicleService.getVehicleType(driverId)).thenReturn(VehicleType.SUV);
        when(driverService.getDriver(driverId)).thenReturn(driver);
        when(trackingDeviceService.getTrackingDevice(driverId)).thenReturn(trackingDevice);
        when(stateMachineConfigurer.createStateMachine(anyString(), any())).thenReturn(sm);
        when(sm.getState()).thenReturn(state);
        when(state.getId()).thenReturn(s);
        //when(s.compareTo(Constants.CAN_MARK_READY_STATE)).thenReturn(0);

        when(sm.sendEvent((Mono<Message>) any()) ).thenReturn(flux);

        when(flux.subscribe()).thenReturn(disposable);
        when(sm.startReactively()).thenReturn(mv);
        when(mv.block()).thenReturn(v);

        // Act
        ResponseEntity<Driver> response = onboardingSMService.markingReady(driverId);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody().isReadyToTakeRides());
        verify(driverService, times(1)).getDriver(driverId);
        verify(trackingDeviceService, times(1)).getTrackingDevice(driverId);
        verify(stateMachineConfigurer, times(1)).createStateMachine(anyString(), any());
        verify(redisStateMachinePersister, times(1)).restore(any(), anyString());
        verify(redisStateMachinePersister, times(1)).persist(any(), anyString());
    }

    @Test
    public void testMarkingReady_WhenDriverCannotMarkReady_ShouldReturnForbidden() throws Exception {
        // Arrange
        int driverId = 1;

        // Mocking
        Driver driver = new Driver();
        driver.setCity("a");
        TrackingDevice trackingDevice = new TrackingDevice();
        trackingDevice.setDelivered(false);
        //when(driver.getCity()).thenReturn("a");
        when(vehicleService.getVehicleType(driverId)).thenReturn(VehicleType.SUV);
        when(driverService.getDriver(driverId)).thenReturn(driver);
        when(trackingDeviceService.getTrackingDevice(driverId)).thenReturn(trackingDevice);
        when(stateMachineConfigurer.createStateMachine(anyString(), any())).thenReturn(sm);
        when(sm.getState()).thenReturn(state);
        when(state.getId()).thenReturn("state_id");

        // Act
        ResponseEntity<Driver> response = onboardingSMService.markingReady(driverId);

        // Assert
        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
        assertNotNull(response.getBody());
        assertFalse(response.getBody().isReadyToTakeRides());
        verify(driverService, times(1)).getDriver(driverId);
        verify(trackingDeviceService, times(1)).getTrackingDevice(driverId);
        verify(stateMachineConfigurer, times(1)).createStateMachine(anyString(), any());
        verify(redisStateMachinePersister, times(1)).restore(any(), anyString());
        verify(redisStateMachinePersister, times(0)).persist(any(), anyString());
    }

    // Add more test cases for the other methods...

    @Test
    public void testGetDriver_ShouldReturnDriver() {
        // Arrange
        int driverId = 1;

        // Mocking
        Driver expectedDriver = new Driver();
        when(driverService.getDriver(driverId)).thenReturn(expectedDriver);

        // Act
        Driver driver = onboardingSMService.getDriver(driverId);

        // Assert
        assertNotNull(driver);
        assertEquals(expectedDriver, driver);
        verify(driverService, times(1)).getDriver(driverId);
    }
}
