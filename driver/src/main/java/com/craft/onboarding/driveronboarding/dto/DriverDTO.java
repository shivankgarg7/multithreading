package com.craft.onboarding.driveronboarding.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public class DriverDTO {

    @NotBlank(message = "First Name is required")
    @Size(min=1,max = 255, message = "First Name cannot exceed 255 characters")
    @NotNull
    @Pattern(regexp = "[A-Za-z]+", message = "Only letters are allowed in First Name")
    private String firstName;

    @NotBlank(message = "Last Name is required")
    @Size(min=1,max = 255, message = "Last Name cannot exceed 255 characters")
    @NotNull
    @Pattern(regexp = "[A-Za-z]+", message = "Only letters are allowed in Last Name")
    private String lastName;

    @NotBlank(message = "License Number is required")
    @Size(min=5,max = 20, message = "License Number cannot exceed 20 characters")
    @NotNull
    @Pattern(regexp = "[A-Za-z0-9]+", message = "Only letters and digits are allowed in Last Name")
    private String licenseNumber;

    @NotBlank(message = "License Number is required")
    @NotNull
    private String city;

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getLicenseNumber() {
        return licenseNumber;
    }

    public void setLicenseNumber(String licenseNumber) {
        this.licenseNumber = licenseNumber;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }


}