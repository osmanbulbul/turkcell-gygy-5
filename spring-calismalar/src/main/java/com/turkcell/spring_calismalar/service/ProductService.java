package com.turkcell.spring_calismalar.service;

import com.turkcell.spring_calismalar.dto.ProductRequest;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;

@Service
public class ProductService {

    // veriler simdilik burada kalsin.
    private List<ProductRequest> productList=new ArrayList<>();
    // is mantigi: urunu eklemeden once kontrol yapabiliriz.
    public String addProduct(ProductRequest request){
        productList.add(request);
        return request.productName+" basariyla eklendí(service uzerinden)";
    }

    public List<ProductRequest>getAllProducts(){
        return productList;
    }

    // public String deleteProduct(){
        
    //     if(productList.isEmpty()){
    //         return "Urun listesi bos, silinecek urun yok.";

    //     }else{
    //         productList.clear();
    //         return "Tum urunler silindi.";
    //     }
    // }
    public String deleteProduct(ProductRequest request){
        
        if(productList.isEmpty()){
            return "Urun listesi bos, silinecek urun yok.";

        }else{
            for(int i=productList.size()-1;i>=0;i--){
                productList.remove(i);
            }
            return "Tum Uurnler silindi.";
        }
    }


}
