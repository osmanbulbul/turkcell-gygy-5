package com.turkcell.spring_starter.service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.turkcell.spring_starter.dto.CreateProductRequest;
import com.turkcell.spring_starter.dto.CreatedProductResponse;
import com.turkcell.spring_starter.dto.GetByIdProductResponse;
import com.turkcell.spring_starter.dto.ListProductResponse;
import com.turkcell.spring_starter.dto.UpdateProductRequest;
import com.turkcell.spring_starter.dto.UpdatedProductResponse;
import com.turkcell.spring_starter.entity.Category;
import com.turkcell.spring_starter.entity.Product;
import com.turkcell.spring_starter.repository.CategoryRepository;
import com.turkcell.spring_starter.repository.ProductRepository;

@Service
public class ProductServiceImpl {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository; // Category'yi doğrulamak için

    public ProductServiceImpl(ProductRepository productRepository,
                              CategoryRepository categoryRepository) {
        this.productRepository = productRepository;
        this.categoryRepository = categoryRepository;
    }

    // ─── CREATE ───────────────────────────────────────────────────────────────
    public CreatedProductResponse create(CreateProductRequest request) {
        // 1. Gelen categoryId gerçekten var mı?
        Category category = categoryRepository.findById(request.getCategoryId())
                .orElseThrow(() -> new RuntimeException("Kategori bulunamadı. ID: " + request.getCategoryId()));

        // 2. Entity oluştur
        Product product = new Product();
        product.setName(request.getName());
        product.setDescription(request.getDescription());
        product.setCategory(category); // sadece ID değil, tüm entity set edilmeli

        product = productRepository.save(product);

        // 3. Response DTO
        CreatedProductResponse response = new CreatedProductResponse();
        response.setId(product.getId());
        response.setName(product.getName());
        response.setDescription(product.getDescription());
        response.setCategoryId(product.getCategory().getId());
        return response;
    }

    // ─── GET ALL ──────────────────────────────────────────────────────────────
    public List<ListProductResponse> getAll() {
        List<Product> products = productRepository.findAll();

        List<ListProductResponse> responseList = new ArrayList<>();
        for (Product product : products) {
            ListProductResponse response = new ListProductResponse();
            response.setId(product.getId());
            response.setName(product.getName());
            response.setDescription(product.getDescription());
            response.setCategoryId(product.getCategory().getId());
            response.setCategoryName(product.getCategory().getName()); // join'li veri
            responseList.add(response);
        }
        return responseList;
    }

    // ─── GET BY ID ────────────────────────────────────────────────────────────
    public GetByIdProductResponse getById(UUID id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Ürün bulunamadı. ID: " + id));

        GetByIdProductResponse response = new GetByIdProductResponse();
        response.setId(product.getId());
        response.setName(product.getName());
        response.setDescription(product.getDescription());
        response.setCategoryId(product.getCategory().getId());
        response.setCategoryName(product.getCategory().getName());
        return response;
    }

    // ─── UPDATE ───────────────────────────────────────────────────────────────
    public UpdatedProductResponse update(UUID id, UpdateProductRequest request) {
        // Ürün var mı?
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Ürün bulunamadı. ID: " + id));

        // Yeni kategori var mı?
        Category category = categoryRepository.findById(request.getCategoryId())
                .orElseThrow(() -> new RuntimeException("Kategori bulunamadı. ID: " + request.getCategoryId()));

        product.setName(request.getName());
        product.setDescription(request.getDescription());
        product.setCategory(category);

        product = productRepository.save(product);

        UpdatedProductResponse response = new UpdatedProductResponse();
        response.setId(product.getId());
        response.setName(product.getName());
        response.setDescription(product.getDescription());
        response.setCategoryId(product.getCategory().getId());
        return response;
    }

    // ─── DELETE ───────────────────────────────────────────────────────────────
    public void delete(UUID id) {
        if (!productRepository.existsById(id)) {
            throw new RuntimeException("Silinecek ürün bulunamadı. ID: " + id);
        }
        productRepository.deleteById(id);
    }
}