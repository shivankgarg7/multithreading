package com.craft.onboarding.driveronboarding.model;

import com.craft.onboarding.driveronboarding.enums.VehicleType;
import jakarta.persistence.*;

@Entity
@Table(name="vehicle")
public class Vehicle {

    @Id
    @Column(unique = true)
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    @Column
    private String vehicleNo;

    @Column
    private String vehicleRegistraionCity;

    @Column
    private String modelNo;

    @Column
    private String brand;

    @Column
    private VehicleType vehicleType;


    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "driver_id", referencedColumnName = "id")
    private Driver driver;


    public Driver getDriver() {
        return driver;
    }

    public void setDriver(Driver driver) {
        this.driver = driver;
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

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

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

    public VehicleType getVehicleType() {
        return vehicleType;
    }

    public void setVehicleType(VehicleType vehicleType) {
        this.vehicleType = vehicleType;
    }



}
