package com.turkcell.spring_starter.service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.turkcell.spring_starter.dto.CreateCategoryRequest;
import com.turkcell.spring_starter.dto.CreatedCategoryResponse;
import com.turkcell.spring_starter.dto.GetByIdCategoryResponse;
import com.turkcell.spring_starter.dto.ListCategoryResponse;
import com.turkcell.spring_starter.dto.UpdateCategoryRequest;
import com.turkcell.spring_starter.dto.UpdatedCategoryResponse;
import com.turkcell.spring_starter.entity.Category;
import com.turkcell.spring_starter.repository.CategoryRepository;

@Service
public class CategoryServiceImpl {

    private final CategoryRepository categoryRepository;

    // Constructor Injection — Spring bu constructor'ı bulur ve CategoryRepository'yi inject eder
    public CategoryServiceImpl(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    // ─── CREATE ───────────────────────────────────────────────────────────────
    public CreatedCategoryResponse create(CreateCategoryRequest request) {
        // Request DTO → Entity dönüşümü
        Category category = new Category();
        category.setName(request.getName());

        // save() → id null ise INSERT, id doluysa UPDATE yapar (JPA kuralı)
        category = categoryRepository.save(category);

        // Entity → Response DTO dönüşümü
        CreatedCategoryResponse response = new CreatedCategoryResponse();
        response.setId(category.getId());
        response.setName(category.getName());
        return response;
    }

    // ─── GET ALL ──────────────────────────────────────────────────────────────
    public List<ListCategoryResponse> getAll() {
        List<Category> categories = categoryRepository.findAll();

        List<ListCategoryResponse> responseList = new ArrayList<>();
        for (Category category : categories) {
            ListCategoryResponse response = new ListCategoryResponse();
            response.setId(category.getId());
            response.setName(category.getName());
            responseList.add(response);
        }
        return responseList;
    }

    // ─── GET BY ID ────────────────────────────────────────────────────────────
    public GetByIdCategoryResponse getById(UUID id) {
        // findById → Optional<Category> döner
        // orElseThrow → bulunamazsa RuntimeException fırlatır (404 mantığı)
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Kategori bulunamadı. ID: " + id));

        GetByIdCategoryResponse response = new GetByIdCategoryResponse();
        response.setId(category.getId());
        response.setName(category.getName());
        return response;
    }

    // ─── UPDATE ───────────────────────────────────────────────────────────────
    public UpdatedCategoryResponse update(UUID id, UpdateCategoryRequest request) {
        // Önce kayıt var mı kontrol et
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Kategori bulunamadı. ID: " + id));

        // Güncelle
        category.setName(request.getName());

        // save() → entity'nin id'si dolu olduğu için UPDATE çalışır
        category = categoryRepository.save(category);

        UpdatedCategoryResponse response = new UpdatedCategoryResponse();
        response.setId(category.getId());
        response.setName(category.getName());
        return response;
    }

    // ─── DELETE ───────────────────────────────────────────────────────────────
    public void delete(UUID id) {
        // Kayıt var mı kontrol et — yoksa anlamlı hata ver
        if (!categoryRepository.existsById(id)) {
            throw new RuntimeException("Silinecek kategori bulunamadı. ID: " + id);
        }
        categoryRepository.deleteById(id);
    }
}