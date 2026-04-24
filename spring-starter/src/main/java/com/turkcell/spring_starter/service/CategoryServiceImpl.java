package com.turkcell.spring_starter.service;

import org.springframework.stereotype.Service;

import com.turkcell.spring_starter.entity.Category;
import com.turkcell.spring_starter.repository.CategoryRepository;

@Service
public class CategoryServiceImpl {
    private final CategoryRepository categoryRepository;

    public CategoryServiceImpl(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    public void create(Category category) {
        // Veritabanında insert-update çalıştır.
        // entity id'e sahipse update
        // entity id'si null ise insert
        this.categoryRepository.save(category);
    } 
}