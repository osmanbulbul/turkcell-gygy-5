package com.turkcell.spring_calismalar.dto;

public class ProductRequest {

    //Springin JSON'i buraya dokebilmesi icin parametreler public veya getter/setter metotlarini kullnamamiz gerekiyor. Teknik bir bilgi olarak, JSON verilerini Java nesnelerine dönüştürmek için kullanılan kütüphaneler (örneğin, Jackson) genellikle sınıfın alanlarına erişim sağlar. Bu nedenle, JSON verilerini doğru şekilde eşleştirebilmek için bu alanların public olması veya getter/setter metotlarının bulunması gerekmektedir. Aksi takdirde, JSON verileri bu alanlara erişemez ve dönüşüm işlemi başarısız olabilir. 

    public  String productName;
    public double productPrice;
    public int productNumber;

    // public String getProductName(){
    //     return productName;
    // }

    // public Double getProductPrice(){
    //     return productPrice;
    // }

    // public int getProductNumber(){
    //     return productNumber;
    // }
    
}
