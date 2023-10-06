package com.craft.onboarding.driveronboarding.controller;

import com.craft.onboarding.driveronboarding.config.DocumentConfig;
import com.craft.onboarding.driveronboarding.enums.VehicleType;
import com.craft.onboarding.driveronboarding.model.Document;
import com.craft.onboarding.driveronboarding.repository.DocumentRepository;
import com.craft.onboarding.driveronboarding.service.DocumentService;
import com.craft.onboarding.driveronboarding.service.DriverService;
import com.craft.onboarding.driveronboarding.service.VehicleService;
import com.craft.onboarding.driveronboarding.utils.JwtTokenUtility;
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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.anyInt;
import static org.mockito.Mockito.when;

public class DocumentControllerTest {

    @InjectMocks
    private DocumentController documentController;

    @Mock
    private DocumentService documentService;

    @Mock
    private JwtTokenUtility jwtTokenUtility;

    @Mock
    private DocumentConfig documentConfig;

    @Mock
    private DriverService driverService;

    @Mock
    DocumentRepository documentRepository;
    @Mock
    private VehicleService vehicleService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testGetDocumentTypes_ShouldReturnDocumentTypes() {
        // Arrange
        String authorizationHeader = "Bearer token";
        String email = "test@example.com";
        List<String> documentTypes = new ArrayList<>();
        documentTypes.add("DocumentType1");
        when(jwtTokenUtility.decodeJwtToken(authorizationHeader)).thenReturn(email);
        when(documentConfig.getDocumentMapPerCity()).thenReturn(createDocumentTypeMap());
        when(driverService.getDriverCity(email)).thenReturn("City");
        when(vehicleService.getVehicleType(anyInt())).thenReturn(VehicleType.SEDAN);
        when(documentService.getAllDocumentTypes(email)).thenReturn(documentTypes);

        // Act
        List<String> response = documentController.getDocumentTypes(authorizationHeader);

        // Assert
        assertNotNull(response);
        assertEquals(documentTypes, response);
    }

    @Test
    public void testUploadDocument_WhenValidDocument_ShouldReturnOk() throws IOException {
        // Arrange
        String authorizationHeader = "Bearer token";
        String documentType = "DocumentType1";
        String email = "test@example.com";
        MultipartFile file = createTestMultipartFile();
        when(jwtTokenUtility.decodeJwtToken(authorizationHeader)).thenReturn(email);
        when(documentConfig.getDocumentMapPerCity()).thenReturn(createDocumentTypeMap());
        when(driverService.getDriverCity(email)).thenReturn("City");
        when(vehicleService.getVehicleType(anyInt())).thenReturn(VehicleType.SEDAN);
        when(documentService.isValid(file)).thenReturn(true);
        when(documentService.uploadDocument(file, documentType, email)).thenReturn(ResponseEntity.ok("File Uploaded"));

        int driverId = 1;
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
        ResponseEntity<String> response = documentController.uploadDocument(file, documentType, authorizationHeader);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("File Uploaded", response.getBody());
    }

    @Test
    public void testUploadDocument_WhenInvalidDocument_ShouldReturnBadRequest() throws IOException {
        // Arrange
        String authorizationHeader = "Bearer token";
        String documentType = "DocumentType1";
        String email = "test@example.com";
        MultipartFile file = createTestMultipartFile();
        when(jwtTokenUtility.decodeJwtToken(authorizationHeader)).thenReturn(email);
        when(documentConfig.getDocumentMapPerCity()).thenReturn(createDocumentTypeMap());
        when(driverService.getDriverCity(email)).thenReturn("City");
        when(vehicleService.getVehicleType(anyInt())).thenReturn(VehicleType.SEDAN);
        when(documentService.isValid(file)).thenReturn(false);

        int driverId = 1;
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
        ResponseEntity<String> response = documentController.uploadDocument(file, documentType, authorizationHeader);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Invalid file", response.getBody());
    }

    @Test
    public void testUploadDocument_WhenFileTypeIsCorrupt_ShouldReturnBadRequest() throws IOException {
        // Arrange
        String authorizationHeader = "Bearer token";
        String documentType = "DocumentType3"; // Not eligible
        String email = "test@example.com";
        MultipartFile file = createTestMultipartFile();
        when(jwtTokenUtility.decodeJwtToken(authorizationHeader)).thenReturn(email);
        when(documentConfig.getDocumentMapPerCity()).thenReturn(createDocumentTypeMap());
        when(driverService.getDriverCity(email)).thenReturn("City");
        when(vehicleService.getVehicleType(anyInt())).thenReturn(VehicleType.SEDAN);
        int driverId = 1;
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
        ResponseEntity<String> response = documentController.uploadDocument(file, documentType, authorizationHeader);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }
    private Map<String, Map<String, List<String>>> createDocumentTypeMap() {
        Map<String, Map<String, List<String>>> documentMap = new HashMap<>();
        Map<String, List<String>> map = new HashMap<>();
        map.put(VehicleType.SEDAN.name(), Arrays.asList("License", "Insurance"));
        documentMap.put("New York", map);
        return documentMap;
    }

    private MultipartFile createTestMultipartFile() throws IOException {
        Path tempFile = Files.createTempFile("test-file", ".png");
        Files.write(tempFile, "test data".getBytes());
        return new MockMultipartFile("test-file", "test-file.png", "image/png", Files.readAllBytes(tempFile));
    }

    // Helper method to create an invalid MultipartFile (not PNG format)
    private MultipartFile createInvalidMultipartFile() throws IOException {
        return new MockMultipartFile("test-file", "test-file.txt", "text/plain", "invalid data".getBytes());
    }
    // Add more test methods for other endpoints in the DocumentController class
}
