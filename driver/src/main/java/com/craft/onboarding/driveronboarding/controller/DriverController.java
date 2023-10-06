package com.craft.onboarding.driveronboarding.controller;

import com.craft.onboarding.driveronboarding.dto.DriverDTO;
import com.craft.onboarding.driveronboarding.dto.VehicleDTO;
import com.craft.onboarding.driveronboarding.model.Vehicle;
import com.craft.onboarding.driveronboarding.service.DriverService;
import com.craft.onboarding.driveronboarding.service.VehicleService;
import com.craft.onboarding.driveronboarding.utils.JwtTokenUtility;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Controller
@Validated
@RequestMapping("/v1")
public class DriverController {


    @Autowired
    DriverService driverService;

    @Autowired
    VehicleService vehicleService;

    @Autowired
    private JwtTokenUtility jwtTokenUtility;

   // @GetMapping("/auth/driver/form")
    @RequestMapping(value = "/auth/driver/form", method = RequestMethod.GET)
    public String showDriverDetailsForm(@ModelAttribute("driverDTO") DriverDTO driverDTO, BindingResult bindingResult, Model model) {
        //hardcoded for POC
        if (bindingResult.hasErrors()) {
            return "driver-details-for";
        }
        model.addAttribute("driverDTO", new DriverDTO());
        model.addAttribute("cities", driverService.getCities());

        return "driver-details-for";
    }

    @PostMapping("/auth/driver/form")
    public String saveDriverDetailsForm(@ModelAttribute("driverDTO") DriverDTO driverDTO, BindingResult bindingResult, @RequestHeader("Authorization") @NotBlank @NotNull String authorizationHeader, Model model){

        model.addAttribute("driverDTO", new DriverDTO());

        if (bindingResult.hasErrors()) {
            return "driver-details-for";
        }
        String email = jwtTokenUtility.decodeJwtToken(authorizationHeader);
        driverService.addDriverDetails(driverDTO, email);
        return "redirect:/auth/vehicle/form";
        //return "redirect:/auth/vehicle/form";
    }

    @GetMapping("/auth/vehicle/form")
    public String addVehicleDetails(@ModelAttribute("vehicleDTO") VehicleDTO vehicleDTO, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            return "VehicleForm";
        }
        model.addAttribute("vehicleDTO", new VehicleDTO());
        model.addAttribute("vehicleTypes", vehicleService.getSupportedVehicleTypesList());

        return "VehicleForm";
    }

    @PostMapping("/auth/vehicle/form")
    public ResponseEntity<Vehicle> saveVehicleDetails(@ModelAttribute("vehicleDTO") VehicleDTO vehicleDTO, BindingResult bindingResult, @RequestHeader("Authorization")  @NotBlank @NotNull String authorizationHeader, Model model) {

        String email = jwtTokenUtility.decodeJwtToken(authorizationHeader);
        Vehicle vehicle = vehicleService.addVehicleDetails(vehicleDTO, email);

        return new ResponseEntity<Vehicle>(vehicle, HttpStatus.OK);
       // return "redirect:/auth/documentTypes";
    }

    @DeleteMapping("/auth/delete/{id}")
    public void deleteDriver(@PathVariable int id){
        driverService.deleteDriver(id);
    }


}
