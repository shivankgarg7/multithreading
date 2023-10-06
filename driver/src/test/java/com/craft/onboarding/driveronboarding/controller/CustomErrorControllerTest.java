package com.craft.onboarding.driveronboarding.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.springframework.ui.Model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class CustomErrorControllerTest {

    @InjectMocks
    private CustomErrorController customErrorController;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testHandleError_ShouldReturnErrorPage() {
        // Arrange
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        Model model = mock(Model.class);

        // Act
        String viewName = customErrorController.handleError(request, response, model);

        // Assert
        assertEquals("error", viewName);
        verify(request, times(1)).getRequestURI();
    }
}
