package com.craft.onboarding.driveronboarding.controller;

import com.craft.onboarding.driveronboarding.config.DocumentConfig;
import com.craft.onboarding.driveronboarding.service.DocumentService;
import com.craft.onboarding.driveronboarding.service.DriverService;
import com.craft.onboarding.driveronboarding.service.VehicleService;
import com.craft.onboarding.driveronboarding.utils.JwtTokenUtility;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@Validated
@RequestMapping("/v1/auth")
public class DocumentController {

    @Autowired
    private  DocumentService documentService;

    @Autowired
    private JwtTokenUtility jwtTokenUtility;

    @Autowired
    private DocumentConfig documentConfig;

    @Autowired
    private DriverService driverService;

    @Autowired
    private VehicleService vehicleService;


    @GetMapping("/documentTypes")
    public List<String> getDocumentTypes(@RequestHeader("Authorization") @NotNull @NotBlank String authorizationHeader) {
        String email = jwtTokenUtility.decodeJwtToken(authorizationHeader);
        return documentService.getAllDocumentTypes(email);
    }


    @PostMapping("/upload/{documentType}")
    public ResponseEntity<String> uploadDocument(
            @RequestParam("file") MultipartFile file,
            @PathVariable("documentType") @NotBlank @NotNull String documentType, @RequestHeader("Authorization") @NotNull @NotBlank  String authorizationHeader
    ) {

        String email = jwtTokenUtility.decodeJwtToken(authorizationHeader);

        List<String> documentTypes = documentConfig.getDocumentMapPerCity().get(driverService.getDriverCity(email))
                .get(vehicleService.getVehicleType(driverService.getDriverId(email)).name());

        if(!documentTypes.contains(documentType)){
            new ResponseEntity("Not Eligible for this document Type", HttpStatus.FORBIDDEN);
        }

        if (file.isEmpty()) {
            return ResponseEntity.badRequest().body("No file provided");
        }

        // Perform custom validations on the file
        if (!documentService.isValid(file)) {
            return ResponseEntity.badRequest().body("Invalid file");
        }
        return documentService.uploadDocument(file, documentType, email);
    }


}
/*
curl -X POST \
 -H "Authorization: Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJzaGl2MDFAZ21haWwiLCJpYXQiOjE2OTU2NDc3NTAsImV4cCI6MTY5NjUxMTc1MH0.aNz5TcXYGey0ltkG7xlVXLXZPwOG0KLbXi_z2pKc0t_ar7IhW9F9xvIFoydqd_z0-ieP3AkNA042qiE0ovfIVw" \
 -F "documentType=NY_Sedan_Document1" -F "file=@/Users/s0g07hd/Desktop/Screenshot 2023-05-19 at 11.45.24 PM.png" http://localhost:8080/auth/upload/LA_SUV_Document1

 */


//admin eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJhZG1pbkBnbWFpbC5jb20iLCJpYXQiOjE2OTYxNzE0NDUsImV4cCI6MTY5NzAzNTQ0NX0.s6yFgd4PncZyelOg27FgK8b-y5TWTGtNvqWDxIAW4BSAlPywjqGfO2vOKH0rNkSctNQeFr78j21kQXpQHOuX4Q
//user1 eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJzaGl2YW5rZ2FyZzAxQGdtYWlsLmNvbSIsImlhdCI6MTY5NjE4MDk0OCwiZXhwIjoxNjk3MDQ0OTQ4fQ.i3FpPDUIcs0rdjfW9FOeqG091PNO6NW4kDpLPBzqmeNZ4O9Ec3qEZNmMKKqsFAe-wSzzbo5r1ctn1ljPJmXUWg