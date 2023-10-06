package com.craft.onboarding.driveronboarding.utils;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.Mockito.*;

public class JwtTokenInterceptorTest {

    @InjectMocks
    private JwtTokenInterceptor jwtTokenInterceptor;

    @Mock
    private JwtTokenUtility jwtTokenUtility;

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void preHandle_ValidToken_ShouldReturnTrue() throws Exception {
        // Arrange
        when(request.getHeader("Authorization")).thenReturn("Bearer validToken");
        when(jwtTokenUtility.decodeJwtToken("validToken")).thenReturn("email");

        // Act
        boolean result = jwtTokenInterceptor.preHandle(request, response, null);

        // Assert
        assert result;
        verify(response, never()).sendError(HttpServletResponse.SC_UNAUTHORIZED, "Invalid or expired token");
        verify(response, never()).sendError(HttpServletResponse.SC_UNAUTHORIZED, "Missing JWT token");
    }

    @Test
    void preHandle_ExpiredToken_ShouldReturnFalse() throws Exception {
        // Arrange
        when(request.getHeader("Authorization")).thenReturn("Bearer expiredToken");
        when(jwtTokenUtility.decodeJwtToken("expiredToken")).thenThrow(ExpiredJwtException.class);

        // Act
        boolean result = jwtTokenInterceptor.preHandle(request, response, null);

        // Assert
        assert !result;
        verify(response).sendError(HttpServletResponse.SC_UNAUTHORIZED, "Invalid or expired token");
        verify(response, never()).sendError(HttpServletResponse.SC_UNAUTHORIZED, "Missing JWT token");
    }

    @Test
    void preHandle_MalformedToken_ShouldReturnFalse() throws Exception {
        // Arrange
        when(request.getHeader("Authorization")).thenReturn("Bearer malformedToken");
        when(jwtTokenUtility.decodeJwtToken("malformedToken")).thenThrow(MalformedJwtException.class);

        // Act
        boolean result = jwtTokenInterceptor.preHandle(request, response, null);

        // Assert
        assert !result;
        verify(response).sendError(HttpServletResponse.SC_UNAUTHORIZED, "Invalid or expired token");
        verify(response, never()).sendError(HttpServletResponse.SC_UNAUTHORIZED, "Missing JWT token");
    }

    @Test
    void preHandle_NoToken_ShouldReturnFalse() throws Exception {
        // Arrange
        when(request.getHeader("Authorization")).thenReturn(null);

        // Act
        boolean result = jwtTokenInterceptor.preHandle(request, response, null);

        // Assert
        assert !result;
        verify(response, never()).sendError(HttpServletResponse.SC_UNAUTHORIZED, "Invalid or expired token");
        verify(response).sendError(HttpServletResponse.SC_UNAUTHORIZED, "Missing JWT token");
    }
}
