package com.turkcell.spring_calismalar.service;

import com.turkcell.spring_calismalar.dto.MaintenanceRequest;
import org.springframework.stereotype.Service;

@Service
public class MaintenanceService {


    public String checkMaintenanceStatus(MaintenanceRequest request){

        int kmAraligi=request.guncelKm-request.sonBakim;
        if(kmAraligi>=10000){
            return "Bakim zamani geldi. Plaka: "+request.plaka;
        }else{
            return "Bakim zamani henüz gelmedi. Plaka: "+request.plaka;
        }
    
    }
}
