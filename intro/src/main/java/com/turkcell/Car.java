package com.turkcell;

// Vehicle'ın tüm özelliklerini yükle, üstüne 
// buraya yazacağım özellikleri de ekle => CAR
public class Car extends Vehicle
{
    private boolean hasSunroof;
    private String[] specs;

    // Encapsulation => Dışarıdan manipülasyona kapalı.
    public String[] getSpecs() {
        return specs.clone();
    }
    public void setSpecs(String[] specs) {
        this.specs = specs.clone();
    }
    // Değerlerini al, referansı alma.
}
