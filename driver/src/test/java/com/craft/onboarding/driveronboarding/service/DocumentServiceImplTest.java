package com.craft.onboarding.driveronboarding.service;


import com.craft.onboarding.driveronboarding.config.DocumentConfig;
import com.craft.onboarding.driveronboarding.enums.VehicleType;
import com.craft.onboarding.driveronboarding.model.Document;
import com.craft.onboarding.driveronboarding.repository.DocumentRepository;
import com.craft.onboarding.driveronboarding.repository.DriverRepository;
import com.craft.onboarding.driveronboarding.utils.Constants;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class DocumentServiceImplTest {

    @InjectMocks
    private DocumentServiceImpl documentService;

    @Mock
    private DocumentRepository documentRepository;

    @Mock
    private DriverService driverService;

    @Mock
    private VehicleService vehicleService;

    @Mock
    private DocumentConfig documentConfig;

    @Mock
    private DriverRepository driverRepository;

    @Mock
    private OnboardingSMService onboardingStateMachineService;

    private static final String UPLOAD_DIR = Constants.UPLOAD_DIR;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testUploadDocument_WhenFileIsValid_ShouldUploadDocument() throws IOException {
        // Arrange
        String email = "test@example.com";
        int driverId = 1;
        String documentType = "License";

        MultipartFile file = createTestMultipartFile();

        when(driverService.getDriverId(email)).thenReturn(driverId);
        when(documentRepository.findByDriverIdAndDocumentType(driverId, documentType)).thenReturn(null);
        when(vehicleService.getVehicleType(1)).thenReturn(VehicleType.SEDAN);
        when(driverService.getDriverId(email)).thenReturn(1);
        when(documentConfig.getDocumentMapPerCity()).thenReturn(createDocumentTypeMap());
        when(driverService.getDriverCity(email)).thenReturn("New York");

        // Act
        ResponseEntity<String> response = documentService.uploadDocument(file, documentType, email);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(driverService, times(2)).getDriverId(email);
        verify(documentRepository, times(1)).findByDriverIdAndDocumentType(driverId, documentType);
        verify(documentRepository, times(1)).save(any(Document.class));
        verify(onboardingStateMachineService, never()).markStartOnboarding(driverId);
    }

    @Test
    public void testUploadDocument_WhenFileIsValidAndAllDocumentsUploaded_ShouldStartOnboarding() throws IOException {
//        documentRepository.findAllByDriverId(driverId).size() ==
//                documentConfig.getDocumentMapPerCity().get(driverService.getDriverCity(email))
//                        .get(vehicleService.getVehicleType(driverService.getDriverId(email)).name()).size()



        // Arrange
        String email = "test@example.com";
        int driverId = 1;
        String documentType = "License";

        MultipartFile file = createTestMultipartFile();
        List<String> allDocumentTypes = Arrays.asList("License", "Insurance"); // Assuming all documents are uploaded

        List<Document> ll = new ArrayList<>();
        ll.add(new Document());
        ll.add(new Document());
        when(driverService.getDriverId(email)).thenReturn(driverId);
        when(documentRepository.findByDriverIdAndDocumentType(driverId, documentType)).thenReturn(null);
        when(documentConfig.getDocumentMapPerCity()).thenReturn(createDocumentTypeMap());
        when(vehicleService.getVehicleType(driverId)).thenReturn(VehicleType.SEDAN);
        when(driverService.getDriverCity(email)).thenReturn("New York");
        when(documentRepository.findAllByDriverId(driverId)).thenReturn(ll);

        // Act
        ResponseEntity<String> response = documentService.uploadDocument(file, documentType, email);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(driverService, times(2)).getDriverId(email);
        verify(documentRepository, times(1)).findByDriverIdAndDocumentType(driverId, documentType);
        verify(documentRepository, times(1)).save(any(Document.class));
        verify(onboardingStateMachineService, times(1)).markStartOnboarding(driverId);
    }

    @Test
    public void testUploadDocument_WhenFileIsInvalid_ShouldNotUploadDocument() throws IOException {
        // Arrange
        String email = "test@example.com";
        String documentType = "License";
        MultipartFile file = createInvalidMultipartFile();
        when(vehicleService.getVehicleType(1)).thenReturn(VehicleType.SEDAN);
        when(driverService.getDriverId(email)).thenReturn(1);
        when(documentConfig.getDocumentMapPerCity()).thenReturn(createDocumentTypeMap());
        when(driverService.getDriverCity(email)).thenReturn("New York");
        // Act
        ResponseEntity<String> response = documentService.uploadDocument(file, documentType, email);


        // Assert
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        verify(driverService, never()).getDriverId(email);
        verify(documentRepository, never()).findByDriverIdAndDocumentType(anyInt(), anyString());
        verify(documentRepository, never()).save(any(Document.class));
        verify(onboardingStateMachineService, never()).markStartOnboarding(anyInt());
    }

    @Test
    public void testIsValid_WhenFileIsValid_ShouldReturnTrue() throws IOException {
        // Arrange
        MultipartFile file = createTestMultipartFile();

        // Act
        boolean isValid = documentService.isValid(file);

        // Assert
        assertTrue(isValid);
    }

    @Test
    public void testIsValid_WhenFileIsInvalid_ShouldThrowException() throws IOException {
        // Arrange
        MultipartFile file = createInvalidMultipartFile();

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> documentService.isValid(file));
    }

    @Test
    public void testGetAllDocumentTypes_ShouldReturnListOfDocumentTypes() {
        // Arrange
        String email = "test@example.com";
        List<String> expectedDocumentTypes = Arrays.asList("License", "Insurance");

        when(driverService.getDriverCity(email)).thenReturn("New York");
        when(vehicleService.getVehicleType(driverService.getDriverId(email))).thenReturn(VehicleType.SEDAN);
        when(documentConfig.getDocumentMapPerCity()).thenReturn(createDocumentTypeMap());

        // Act
        List<String> documentTypes = documentService.getAllDocumentTypes(email);

        // Assert
        assertEquals(expectedDocumentTypes, documentTypes);
    }

    // Helper method to create a test MultipartFile
    private MultipartFile createTestMultipartFile() throws IOException {
        Path tempFile = Files.createTempFile("test-file", ".png");
        Files.write(tempFile, "test data".getBytes());
        return new MockMultipartFile("test-file", "test-file.png", "image/png", Files.readAllBytes(tempFile));
    }

    // Helper method to create an invalid MultipartFile (not PNG format)
    private MultipartFile createInvalidMultipartFile() throws IOException {
        return new MockMultipartFile("test-file", "test-file.txt", "text/plain", "invalid data".getBytes());
    }

    // Helper method to create a sample document type map
    private Map<String, Map<String, List<String>>> createDocumentTypeMap() {
        Map<String, Map<String, List<String>>> documentMap = new HashMap<>();
        Map<String, List<String>> map = new HashMap<>();
        map.put(VehicleType.SEDAN.name(), Arrays.asList("License", "Insurance"));
        documentMap.put("New York", map);
        return documentMap;
    }
}
