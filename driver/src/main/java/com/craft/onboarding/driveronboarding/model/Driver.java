package com.craft.onboarding.driveronboarding.model;


import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;

import java.util.List;
import java.util.Set;

@Entity
@Table(name="driver", uniqueConstraints=
@UniqueConstraint(columnNames = {"id", "email"}))
//@IdClass(DriverCompositeID.class)
public class Driver {

    @Id
    @Column(unique = true)
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;
    @Column(unique = true)
    private String email;
    @Column
    private String firstName;
    @Column
    private String lastName;

    @Column
    private String city;

    @Column
    private String licenseNumber;

    @Column
    private boolean hasOnboardingStarted;

    @Column
    private boolean isReadyToTakeRides;

    @Column
    private String state;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "user_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    private Set<Role> roles;

    @OneToMany(mappedBy = "driver")
    private List<Document> documents;

    @OneToOne(mappedBy = "driver",cascade = CascadeType.ALL)
    private Vehicle vehicle;

    @OneToOne(mappedBy = "driver", cascade = CascadeType.ALL)
    private TrackingDevice trackingDevice;


    public String getState() {
        return state;
    }

    public Set<Role> getRoles() {
        return roles;
    }

    public void setRoles(Set<Role> roles) {
        this.roles = roles;
    }

    public String isState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public boolean isHasOnboardingStarted() {
        return hasOnboardingStarted;
    }

    public void setHasOnboardingStarted(boolean hasOnboardingStarted) {
        this.hasOnboardingStarted = hasOnboardingStarted;
    }

    public boolean isReadyToTakeRides() {
        return isReadyToTakeRides;
    }

    public void setReadyToTakeRides(boolean readyToTakeRides) {
        isReadyToTakeRides = readyToTakeRides;
    }

    public String getLicenseNumber() {
        return licenseNumber;
    }

    public void setLicenseNumber(String licenseNumber) {
        this.licenseNumber = licenseNumber;
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }


    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }



    public Driver() {
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }


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



    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }


}
