package com.turkcell.spring_starter.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.turkcell.spring_starter.dto.CreateCategoryRequest;
import com.turkcell.spring_starter.dto.CreatedCategoryResponse;
import com.turkcell.spring_starter.dto.ListCategoryResponse;
import com.turkcell.spring_starter.entity.Category;
import com.turkcell.spring_starter.service.CategoryServiceImpl;

import java.util.List;



// Bu projedeki tum enttiyler icin TUM CRUD islemleri kodlanbmali
// get get by id add update delete islemlerini teker teker yapacagiz.
// Arastirma ODEV ISE SU : JPQL nedir ne icin kullanlir.Bunun kullanimina bakacagiz.
// Kutuphane sistemimizi code-first olsturun. Koda dokulmus halini yapacagiz.,


import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@RestController
@RequestMapping("/api/categories")
public class CategoriesController {
    private final CategoryServiceImpl categoryServiceImpl;

    public CategoriesController(CategoryServiceImpl categoryServiceImpl) {
        this.categoryServiceImpl = categoryServiceImpl;
    }

    @PostMapping()
    public CreatedCategoryResponse create(@RequestBody CreateCategoryRequest createCategoryRequest)
    {
       return categoryServiceImpl.create(createCategoryRequest);
    }

    @GetMapping
    public List<ListCategoryResponse> getAll() {
        return categoryServiceImpl.getAll();
    }
}