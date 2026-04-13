package com.turkcell.spring_starter.dto;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PositiveOrZero;
import org.hibernate.validator.constraints.Length;
import jakarta.validation.constraints.Pattern;

public class ProductForCreateDto {

    @NotBlank // alana ozel anotasyonlar
    @Length(min = 2)
    @Pattern(regexp="^[^0-9]*$")
    private String name;

    @PositiveOrZero
    private Double price;
    
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public Double getPrice() {
        return price;
    }
    public void setPrice(Double price) {
        this.price = price;
    }
}