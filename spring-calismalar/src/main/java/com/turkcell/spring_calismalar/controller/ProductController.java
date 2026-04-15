package com.turkcell.spring_calismalar.controller;

import com.turkcell.spring_calismalar.dto.ProductRequest;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
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

    // @PostMapping("/add")
    // public ResponseEntity<String> addProduct(@RequestBody ProductRequest request){

    //     // basit bir kontrol (is mantigi/ business logic baslangici)
    //     if(request.productName==null||request.productName.isEmpty()){
    //         //hatali istek olma durumu
    //         return ResponseEntity
    //                 .status(HttpStatus.BAD_REQUEST)
    //                 .body("hatali durum, Urun ismi bos olamaz.");
    //     }

    //     //basarili kayit durumu 201 Created
    //     String mesaj=request.productName+" Basaryia sisteme kaydedildi.";
    //     return new ResponseEntity<>(mesaj,HttpStatus.CREATED);
    // }

    @GetMapping("/not-sorgula")
    public ResponseEntity<String> getSinavSonucu(@RequestParam int not) {

        if (not < 0 || not > 100) {
            // Durum: Geçersiz veri gönderildi
            // Etiket: 400 Bad Request
            return new ResponseEntity<>("Hata: Not 0-100 arasında olmalıdır!", HttpStatus.BAD_REQUEST);
        }

        if (not >= 50) {
            // Durum: Başarılı ve Geçti
            // Etiket: 200 OK
            return new ResponseEntity<>("Tebrikler, dersten geçtiniz!", HttpStatus.OK);
        } else {
            // Durum: Başarılı ama Kaldı
            // Etiket: 200 OK (Çünkü işlem teknik olarak başarılı, sadece sonuç olumsuz)
            return new ResponseEntity<>("Maalesef dersten kaldınız.", HttpStatus.OK);
        }
    }

    @PostMapping("/urun-goster")
    public String getUrun(@RequestBody ProductRequest request){
        return request.productName+" isimli urun gosterildi. \nFiyat: "+request.productPrice+ " Tl.\nAdet: "+request.productNumber; 
    }


}
