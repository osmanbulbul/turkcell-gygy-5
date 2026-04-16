package com.turkcell.spring_calismalar.controller;

import com.turkcell.spring_calismalar.dto.ProductRequest;
import com.turkcell.spring_calismalar.service.ProductService;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;

import java.util.List;

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

    // @GetMapping("/not-sorgula")
    // public ResponseEntity<String> getSinavSonucu(@RequestParam int not) {

    //     if (not < 0 || not > 100) {
    //         // Durum: Geçersiz veri gönderildi
    //         // Etiket: 400 Bad Request
    //         return new ResponseEntity<>("Hata: Not 0-100 arasında olmalıdır!", HttpStatus.BAD_REQUEST);
    //     }

    //     if (not >= 50) {
    //         // Durum: Başarılı ve Geçti
    //         // Etiket: 200 OK
    //         return new ResponseEntity<>("Tebrikler, dersten geçtiniz!", HttpStatus.OK);
    //     } else {
    //         // Durum: Başarılı ama Kaldı
    //         // Etiket: 200 OK (Çünkü işlem teknik olarak başarılı, sadece sonuç olumsuz)
    //         return new ResponseEntity<>("Maalesef dersten kaldınız.", HttpStatus.OK);
    //     }
    // }

    // @PostMapping("/urun-goster")
    // public String getUrun(@RequestBody ProductRequest request){
    //     return request.getProductName()+" isimli urun gosterildi. Fiyat: "+request.getProductPrice()+" TL/Adet: "+request.getProductNumber();
    // }


    // ProductService de yapilari olusturduk, simdi controller uzerinden cagiralim.
    //basitce artik controller listreye ekleme yapmayacak , isi service e devredecek. Burada da devreye denpendecy Injection girecek.
    // daha basitcesi Controller , bana isi yapacak service lazim diyor ve Spring ona bu nesneyi otomatik verityor.


    private final ProductService productService;// artik lsiretyi burada tutmuyoruz, service yi cagiriyoruz.

    // constructor injection (en saglikli yontem budur,.)
    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @PostMapping("/add")
    public ResponseEntity<String> addProduct(@RequestBody ProductRequest request){

        //burada isi service ye pasliyoruz.
        String result=productService.addProduct(request);
        return new ResponseEntity<>(result,HttpStatus.CREATED);
    }

    @GetMapping("/all")
    public ResponseEntity<List<ProductRequest>> getAllProducts(){

        //vberiyi serviceden iste.
        return new ResponseEntity<>(productService.getAllProducts(),HttpStatus.OK);
    }

    @PostMapping("/delete")
    public ResponseEntity<String> deleteProducts(@RequestBody ProductRequest request){
        String result=productService.deleteProduct(request);
        return new ResponseEntity<>(result,HttpStatus.OK);
    }

    // @DeleteMapping("/delete")
    // public ResponseEntity<String> deleteAllProducts(){
    //     String result=productService.deleteProduct();
    //     return new ResponseEntity<>(result,HttpStatus.OK);
    // }
    
}
