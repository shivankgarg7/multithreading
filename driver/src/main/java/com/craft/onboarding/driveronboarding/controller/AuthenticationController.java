package com.craft.onboarding.driveronboarding.controller;

import com.craft.onboarding.driveronboarding.dto.AuthDTO;
import com.craft.onboarding.driveronboarding.model.Driver;
import com.craft.onboarding.driveronboarding.service.DriverService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;


@Validated
@Controller
@RequestMapping("/v1")
public class AuthenticationController {

    @Autowired
    DriverService driverService;


    @PostMapping("/signup")
    public ResponseEntity<Driver> registerDriver(@RequestBody @Valid AuthDTO authDTO) {


        Driver driver = driverService.signup(authDTO);
        return new ResponseEntity(driver, HttpStatus.CREATED);

    }


    @PostMapping("/login")
    public ResponseEntity<String> loginDriver(@RequestBody @Valid AuthDTO authDTO) {

        String token = driverService.login(authDTO);
        return ResponseEntity.ok(token);

    }
    //eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJzaGl2YW5rZ2FyZzAxQGdtYWlsLmNvbSIsImlhdCI6MTY5NjA4NjY0NiwiZXhwIjoxNjk2OTUwNjQ2fQ.mJUVYHFhhlMMTWvwPpBSp1sw_I_Oc32En-kwSYmgJRPHhrhd0wzRdaAmbYFlJmpLblapB6UbStewbOXTjw-ADw
}

