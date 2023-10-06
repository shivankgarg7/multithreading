package com.craft.onboarding.driveronboarding.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

@Entity
@Table(name="tracking_device", uniqueConstraints=
@UniqueConstraint(columnNames = {"driver_id"}))
public class TrackingDevice {
    @Id
    @Column(unique = true)
    @GeneratedValue(strategy = GenerationType.UUID)
    @NotNull
    private UUID id;

    private boolean isDelivered;


    public Driver getDriver() {
        return driver;
    }

    public void setDriver(Driver driver) {
        this.driver = driver;
    }

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "driver_id", referencedColumnName = "id")
    private Driver driver;

    public boolean isDelivered() {
        return isDelivered;
    }

    public void setDelivered(boolean delivered) {
        isDelivered = delivered;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }


}
