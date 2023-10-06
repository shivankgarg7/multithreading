package com.craft.onboarding.driveronboarding.repository;

import com.craft.onboarding.driveronboarding.model.Document;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface DocumentRepository  extends JpaRepository<Document, UUID> {

    Document findByDriverIdAndDocumentType(int driverId, String documentType);

    List<Document> findAllByDriverId(int driverId);



}
