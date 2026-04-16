package com.turkcell.spring_calismalar.controller;

import com.turkcell.spring_calismalar.dto.MaintenanceRequest;
import com.turkcell.spring_calismalar.service.MaintenanceService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@RestController
@RequestMapping("/api/maintenance")
public class MaintenanceController {

    private final MaintenanceService maintenanceService;

    //dependency injection... 
    public MaintenanceController(MaintenanceService maintenanceService){
        this.maintenanceService=maintenanceService;
    }


    @PostMapping("/check")
    public ResponseEntity<String> checkCar(@RequestBody MaintenanceRequest request){

        String report=maintenanceService.checkMaintenanceStatus(request);
        return new ResponseEntity<>(report,HttpStatus.OK);
    }
}
