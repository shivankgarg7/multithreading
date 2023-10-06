package com.craft.onboarding.driveronboarding.service;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
public interface DocumentService {

    public ResponseEntity<String> uploadDocument(MultipartFile file, String documentType, String authorisation) ;

    boolean isValid(MultipartFile file);

    List<String> getAllDocumentTypes(String authorizationHeader);
}