package com.craft.onboarding.driveronboarding.dto;

import com.craft.onboarding.driveronboarding.enums.VehicleType;

public class VehicleDTO {

    private String vehicleNo;

    private String vehicleRegistraionCity;

    private String modelNo;

    private String brand;

    public String getVehicleNo() {
        return vehicleNo;
    }

    public void setVehicleNo(String vehicleNo) {
        this.vehicleNo = vehicleNo;
    }

    public String getVehicleRegistraionCity() {
        return vehicleRegistraionCity;
    }

    public void setVehicleRegistraionCity(String vehicleRegistraionCity) {
        this.vehicleRegistraionCity = vehicleRegistraionCity;
    }

    public String getModelNo() {
        return modelNo;
    }

    public void setModelNo(String modelNo) {
        this.modelNo = modelNo;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public VehicleType getVehicleType() {
        return vehicleType;
    }

    public void setVehicleType(VehicleType vehicleType) {
        this.vehicleType = vehicleType;
    }

    private VehicleType vehicleType;

}
