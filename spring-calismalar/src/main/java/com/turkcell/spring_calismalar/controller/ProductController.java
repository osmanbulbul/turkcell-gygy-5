package com.turkcell.spring_calismalar.controller;

import com.turkcell.spring_calismalar.dto.ProductRequest;
import org.springframework.web.bind.annotation.RequestMapping;
// import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;

@RestController
@RequestMapping("/api/products")
public class ProductController {

    @GetMapping("/test")
    public String deneme(){
        return "Test icin gonderildi.";
    }

    
    // @PostMapping("/add")
    // public String addProduct(@RequestParam String productName,@RequestParam double productPrice,@RequestParam int productNumber){
    //     return productName+" isimli urun eklendi. Fiyat: "+productPrice+" TL.Adet :"+productNumber;
    // }

    // @PostMapping("/add")
    // public String addProduct(@RequestBody ProductRequest request){
    //     return request.productName+" isimli urun eklendi. Fiyat: "+request.productPrice+" TL. Adet :"+request.productNumber;
    // }

    @PostMapping("/add")
    public ResponseEntity<String> addProduct(@RequestBody ProductRequest request){

        // basit bir kontrol (is mantigi/ business logic baslangici)
        if(request.productName==null||request.productName.isEmpty()){
            //hatali istek olma durumu
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body("hatali durum, Urun ismi bos olamaz.");
        }

        //basarili kayit durumu 201 Created
        String mesaj=request.productName+" Basaryia sisteme kaydedildi.";
        return new ResponseEntity<>(mesaj,HttpStatus.CREATED);
    }


}
