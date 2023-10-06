package com.craft.onboarding.driveronboarding.exception;

public class DriverNotFoundException  extends RuntimeException {
    public DriverNotFoundException(String message) {
        super(message);
    }
}