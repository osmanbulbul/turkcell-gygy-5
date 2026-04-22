package com.turkcell.spring_calismalar.service;

import com.turkcell.spring_calismalar.dto.ProductRequest;

import java.util.List;

import org.springframework.stereotype.Service;
import com.turkcell.spring_calismalar.repository.ProductRepository;
import com.turkcell.spring_calismalar.entity.Product;


@Service
public class ProductService {

    // // veriler simdilik burada kalsin.
    // private List<ProductRequest> productList=new ArrayList<>();
    // // is mantigi: urunu eklemeden once kontrol yapabiliriz.
    // public String addProduct(ProductRequest request){
    //     productList.add(request);
    //     return request.productName+" basariyla eklendí(service uzerinden)";
    // }

    // public List<ProductRequest>getAllProducts(){
    //     return productList;
    // }

    // public String deleteProduct(){
        
    //     if(productList.isEmpty()){
    //         return "Urun listesi bos, silinecek urun yok.";

    //     }else{
    //         productList.clear();
    //         return "Tum urunler silindi.";
    //     }
    // }
    // public String deleteProduct(ProductRequest request){
        
    //     if(productList.isEmpty()){
    //         return "Urun listesi bos, silinecek urun yok.";

    //     }else{
    //         for(int i=productList.size()-1;i>=0;i--){
    //             productList.remove(i);
    //         }
    //         return "Tum Uurnler silindi.";
    //     }
    // }


    // eski array list kaldirdik, yerine Repository'i aldik.
    private final ProductRepository productRepository;

    //dependecy injection yaparak ProductRepository'i service katmanina enjekte ediyoruz. Spring bunu otomatik olarak yapar, biz sadece constructor olustururuz.
    public ProductService(ProductRepository productRepository){
        this.productRepository=productRepository;
    }

    // urun ekleme islemi
    public String addProduct(Product product){
        productRepository.save(product);
        return "Urun veritabanina eklendi.";
    }

    //tum urunleri getirme islemi
    public List<Product>getAllProducts(){
        return productRepository.findAll();// eskiden productList idi , simdi findAll().
    }

    //tum ruunleri silme islemi
    public String clearAll(){
        productRepository.deleteAll();
        return "Tum urunler silindi.";
    }


}
