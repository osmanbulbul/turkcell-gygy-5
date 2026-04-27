package com.turkcell.spring_starter.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class UpdateCategoryRequest {

    @NotBlank(message = "Kategori adı boş olamaz")
    @Size(max = 100, message = "Kategori adı en fazla 100 karakter olabilir")
    private String name;

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
}