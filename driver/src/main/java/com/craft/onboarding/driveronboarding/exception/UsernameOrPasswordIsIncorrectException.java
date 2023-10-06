package com.craft.onboarding.driveronboarding.exception;

public class UsernameOrPasswordIsIncorrectException extends RuntimeException {
    public UsernameOrPasswordIsIncorrectException(String message) {
        super(message);
    }
}