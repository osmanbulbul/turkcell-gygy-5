package com.turkcell.spring_calismalar.entity;

import jakarta.persistence.*;// entity ve table icin import etttik ,ayri ayri da yazabilirdim,

@Entity // bu bir entity yani bu sinifin bir veritabani tablosu oldugunu soyler. JPA bunu anlar ve bu sinifin bir tabloya karslik geldigini bilir.
@Table(name="products")//veritabanindaki tablo adini belirler

public class Product {

    @Id // primary key essiz oldugunnu belirtir.
    @GeneratedValue(strategy=GenerationType.IDENTITY) // idnin otomatik artmasini saglar.

    private Long id;

    @Column(name="product_name",nullable=false) // veritabanindaki kolon adini belirler ve nullable=false ile bu kolonun bos gecilemeyecegini belirtir.
    private String productName;

    private double price;
    private int productNumber;

    // BOS Constructor jpa icin zorunludur....!!!!!
    public Product(){

    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getProductNumber() {
        return productNumber;
    }

    public void setProductNumber(int productNumber) {
        this.productNumber = productNumber;
    }
}
