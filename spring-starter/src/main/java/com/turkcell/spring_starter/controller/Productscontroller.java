package com.turkcell.spring_starter.controller;

import java.util.List;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import com.turkcell.spring_starter.dto.CreateProductRequest;
import com.turkcell.spring_starter.dto.CreatedProductResponse;
import com.turkcell.spring_starter.dto.GetByIdProductResponse;
import com.turkcell.spring_starter.dto.ListProductResponse;
import com.turkcell.spring_starter.dto.UpdateProductRequest;
import com.turkcell.spring_starter.dto.UpdatedProductResponse;
import com.turkcell.spring_starter.service.ProductServiceImpl;

@RestController
@RequestMapping("/api/products")
public class Productscontroller {

    private final ProductServiceImpl productServiceImpl;

    public Productscontroller(ProductServiceImpl productServiceImpl) {
        this.productServiceImpl = productServiceImpl;
    }

    // POST /api/products
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CreatedProductResponse create(@Validated @RequestBody CreateProductRequest request) {
        return productServiceImpl.create(request);
    }

    // GET /api/products
    @GetMapping
    public List<ListProductResponse> getAll() {
        return productServiceImpl.getAll();
    }

    // GET /api/products/{id}
    @GetMapping("/{id}")
    public GetByIdProductResponse getById(@PathVariable UUID id) {
        return productServiceImpl.getById(id);
    }

    // PUT /api/products/{id}
    @PutMapping("/{id}")
    public UpdatedProductResponse update(@PathVariable UUID id,
                                         @Validated @RequestBody UpdateProductRequest request) {
        return productServiceImpl.update(id, request);
    }

    // DELETE /api/products/{id}
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable UUID id) {
        productServiceImpl.delete(id);
    }
}