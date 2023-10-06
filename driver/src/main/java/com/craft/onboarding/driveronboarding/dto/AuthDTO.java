package com.craft.onboarding.driveronboarding.dto;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public class AuthDTO {

    @NotBlank(message = "Field Email is required")
    @Size(min=5,max = 255, message = "Email cannot exceed 255 characters")
    @NotNull
    @Pattern(regexp = "[A-Za-z0-9]+@[a-z]+\\.com", message = "Only letters and digits are allowed before @'email-provider'.com")
    public String email;

    @NotBlank(message = "Field Password is required")
    @Size(min = 5, max = 21, message = "Password cannot exceed 255 characters and cannot be less than 5 characters")
    @NotNull
    public String password;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }


}

