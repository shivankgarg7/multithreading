package com.craft.onboarding.driveronboarding.model;

import jakarta.persistence.*;

import java.util.UUID;

@Entity
@Table(name="document", uniqueConstraints=
@UniqueConstraint(columnNames = {"documentType", "driver_id"}))
public class Document {

    @Id
    @Column(unique = true)
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column
    private String documentName;

    public String getDocumentType() {
        return documentType;
    }

    public void setDocumentType(String documentType) {
        this.documentType = documentType;
    }

    @Column
    private String documentType;

    public Driver getDriver() {
        return driver;
    }

    public void setDriver(Driver driver) {
        this.driver = driver;
    }

    @ManyToOne
    @JoinColumn(name = "driver_id", nullable = false)
    private Driver driver;


    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getDocumentName() {
        return documentName;
    }

    public void setDocumentName(String documentName) {
        this.documentName = documentName;
    }


}
