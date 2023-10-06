package com.craft.onboarding.driveronboarding.service;

import com.craft.onboarding.driveronboarding.config.DocumentConfig;
import com.craft.onboarding.driveronboarding.model.Document;
import com.craft.onboarding.driveronboarding.repository.DocumentRepository;
import com.craft.onboarding.driveronboarding.utils.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;


@Service
public class DocumentServiceImpl implements DocumentService {

    private static final String UPLOAD_DIR = Constants.UPLOAD_DIR;

    @Autowired
    private DocumentRepository documentRepository;

    @Autowired
    private DriverService driverService;

    @Autowired
    private VehicleService vehicleService;

    @Autowired
    private DocumentConfig documentConfig;

    @Autowired
    private OnboardingSMService onboardingStateMachineService;


    @Override
    public ResponseEntity<String> uploadDocument(MultipartFile file, String documentType, String email) {
        try {
            // Ensure the upload directory exists, create it if necessary
            File uploadDir = new File(UPLOAD_DIR);
            if (!uploadDir.exists()) {
                uploadDir.mkdirs();
            }

            // Check if the uploaded file is in PNG format
            if(!file.getContentType().equals("image/png")){
                return new ResponseEntity<>("File format not supported", HttpStatus.INTERNAL_SERVER_ERROR);
            }

            int driverId = driverService.getDriverId(email);

            String uniqueFileName = driverId + "_" + System.currentTimeMillis() + ".png";

            // Save the uploaded file to the filesystem
            Path filePath = Path.of(UPLOAD_DIR, uniqueFileName);
            Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

            Document alreadyPresentDocumentofThisType = documentRepository.findByDriverIdAndDocumentType(driverId, documentType);


            if(alreadyPresentDocumentofThisType != null){
                alreadyPresentDocumentofThisType.setDocumentName(uniqueFileName);
                documentRepository.save(alreadyPresentDocumentofThisType);

            }
            else {
                // Create a Document entity and set its properties
                Document document = new Document();
                document.setDriver(driverService.getDriver(driverId));
                document.setDocumentName(uniqueFileName);
                document.setDocumentType(documentType);
                documentRepository.save(document);

            }
            
            if(checkIfallDocumentsUploaded(email, driverId)){
                onboardingStateMachineService.markStartOnboarding(driverId);
                return ResponseEntity.ok("File Uploaded And Onboarding started");

            }

            // Save the Document entity to the database

            return ResponseEntity.ok("File Uploaded");
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>("File not Uploaded", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    private boolean checkIfallDocumentsUploaded(String email, int driverId) {
        if(
                documentRepository.findAllByDriverId(driverId).size() ==
                documentConfig.getDocumentMapPerCity().get(driverService.getDriverCity(email))
                .get(vehicleService.getVehicleType(driverService.getDriverId(email)).name()).size()
        )
            return true;
        else
            return false;
    }


    @Override
    public boolean isValid(MultipartFile file){
        if (!file.getContentType().equals("image/png")) {
            throw new IllegalArgumentException("Only PNG files are allowed.");
        }
        return true;
    }

    public List<String> getAllDocumentTypes(String email) {
        List<String> allDocumentTypes = new ArrayList<>();
//        System.out.println("this.documentConfig.get(driverService.getDriverCity(authorisation))="+this.documentConfig.get(driverService.getDriverCity(authorisation)) + "  " +
//                "documentConfig=" + documentConfig + "   vehicle type = "+ vehicleService.getVehicleType(driverService.getDriverId(authorisation)) + "   this.documentConfig.get(driverService.getDriverCity(authorisation)).get(vehicleService.getVehicleType(driverService.getDriverId(authorisation)))" + this.documentConfig.get(driverService.getDriverCity(authorisation)).get(vehicleService.getVehicleType(driverService.getDriverId(authorisation))));


        for (String documentType : documentConfig.getDocumentMapPerCity().get(driverService.getDriverCity(email)).get(vehicleService.getVehicleType(driverService.getDriverId(email)).name())) {
            allDocumentTypes.add(documentType);
        }

        return allDocumentTypes;
    }


}