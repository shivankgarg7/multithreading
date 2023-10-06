package com.craft.onboarding.driveronboarding.utils;

import com.craft.onboarding.driveronboarding.exception.AdminAccesIsNotGRantedException;
import com.craft.onboarding.driveronboarding.model.Driver;
import com.craft.onboarding.driveronboarding.model.Role;
import com.craft.onboarding.driveronboarding.service.DriverService;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.util.ReflectionTestUtils;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class RoleBasedAccessInterceptorTest {

    @InjectMocks
    private RoleBasedAccessInterceptor interceptor;

    @Mock
    private JwtTokenUtility jwtTokenUtility;

    @Mock
    private DriverService driverService;

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        ReflectionTestUtils.setField(interceptor, "jwtTokenUtility", jwtTokenUtility);
        ReflectionTestUtils.setField(interceptor, "driverService", driverService);
    }

    @Test
    public void testPreHandleValidTokenWithAdminRole() throws Exception {


        // Mocking JWT token
        String jwtToken = "valid-token";
        when(request.getHeader("Authorization")).thenReturn("Bearer " + jwtToken);

        // Mocking token decoding
        String userEmail = "user@example.com";
        when(jwtTokenUtility.decodeJwtToken(jwtToken)).thenReturn(userEmail);

        // Mocking driver with admin role
        Driver driver = new Driver();
        Role adminRole = new Role();
        adminRole.setAuthority("ROLE_ADMIN");
        Set<Role> roles = new HashSet<>();
        roles.add(adminRole);
        driver.setRoles(roles);
        when(driverService.getDriver(userEmail)).thenReturn(driver);

        boolean result = interceptor.preHandle(request, response, null);

        assertTrue(result);
        assertEquals(0, response.getStatus());
        verify(response, never()).sendError(anyInt(), anyString());
    }

    @Test
    public void testPreHandleValidTokenWithoutAdminRole() throws Exception {


        // Mocking JWT token
        String jwtToken = "valid-token";
        when(request.getHeader("Authorization")).thenReturn("Bearer " + jwtToken);

        // Mocking token decoding
        String userEmail = "user@example.com";
        when(jwtTokenUtility.decodeJwtToken(jwtToken)).thenReturn(userEmail);

        // Mocking driver without admin role
        Driver driver = new Driver();
        Role driverRole = new Role();
        driverRole.setAuthority("ROLE_DRIVER");
        Set<Role> roles = new HashSet<>();
        roles.add(driverRole);
        driver.setRoles(roles);
        when(driverService.getDriver(userEmail)).thenReturn(driver);

        boolean result = interceptor.preHandle(request, response, null);

        assertFalse(result);
//        assertEquals(HttpServletResponse.SC_UNAUTHORIZED, response.getStatus());
        verify(response).sendError(HttpServletResponse.SC_UNAUTHORIZED, "Does not have Admin Access");
    }

    @Test
    public void testPreHandleInvalidToken() throws Exception {


        // Mocking invalid JWT token
        String jwtToken = "invalid-token";
        when(request.getHeader("Authorization")).thenReturn("Bearer " + jwtToken);

        // Mocking token decoding to throw an exception
        when(jwtTokenUtility.decodeJwtToken(jwtToken)).thenThrow(new ExpiredJwtException(null, null, "Token expired"));

        boolean result = interceptor.preHandle(request, response, null);

        assertFalse(result);
//        assertEquals(HttpServletResponse.SC_UNAUTHORIZED, response.getStatus());
        verify(response).sendError(HttpServletResponse.SC_UNAUTHORIZED, "Invalid or expired token");
    }

    @Test
    public void testPreHandleMissingToken() throws Exception {


        // Missing JWT token in the request header
        when(request.getHeader("Authorization")).thenReturn(null);

        boolean result = interceptor.preHandle(request, response, null);

        assertFalse(result);
//        assertEquals(HttpServletResponse.SC_UNAUTHORIZED, response.getStatus());
        verify(response).sendError(HttpServletResponse.SC_UNAUTHORIZED, "Missing JWT token");
    }

    @Test
    public void testPreHandleIOException() throws Exception {

        // Mocking JWT token
        String jwtToken = "valid-token";
        when(request.getHeader("Authorization")).thenReturn("Bearer " + jwtToken);

        // Mocking token decoding
        String userEmail = "user@example.com";
        when(jwtTokenUtility.decodeJwtToken(jwtToken)).thenReturn(userEmail);

        // Mocking driver without admin role
        Driver driver = new Driver();
        Role driverRole = new Role();
        driverRole.setAuthority("ROLE_DRIVER");
        Set<Role> roles = new HashSet<>();
        roles.add(driverRole);
        driver.setRoles(roles);
        when(driverService.getDriver(userEmail)).thenReturn(driver);

        // Mocking response to throw IOException
        doThrow(new IOException()).when(response).sendError(HttpServletResponse.SC_UNAUTHORIZED, "Does not have Admin Access");

        assertThrows(AdminAccesIsNotGRantedException.class, () -> interceptor.preHandle(request, response, null));
    }
}
