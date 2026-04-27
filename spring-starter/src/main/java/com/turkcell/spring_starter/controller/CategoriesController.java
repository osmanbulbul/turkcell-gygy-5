package com.turkcell.spring_starter.controller;

import java.util.List;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import com.turkcell.spring_starter.dto.CreateCategoryRequest;
import com.turkcell.spring_starter.dto.CreatedCategoryResponse;
import com.turkcell.spring_starter.dto.GetByIdCategoryResponse;
import com.turkcell.spring_starter.dto.ListCategoryResponse;
import com.turkcell.spring_starter.dto.UpdateCategoryRequest;
import com.turkcell.spring_starter.dto.UpdatedCategoryResponse;
import com.turkcell.spring_starter.service.CategoryServiceImpl;

@RestController
@RequestMapping("/api/categories")
public class CategoriesController {

    private final CategoryServiceImpl categoryServiceImpl;

    public CategoriesController(CategoryServiceImpl categoryServiceImpl) {
        this.categoryServiceImpl = categoryServiceImpl;
    }

    // POST /api/categories
    // @Validated → Request body üzerindeki @NotBlank, @Size gibi validationları tetikler
    // @ResponseStatus(201) → Başarılı create işleminde HTTP 201 Created döner
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CreatedCategoryResponse create(@Validated @RequestBody CreateCategoryRequest request) {
        return categoryServiceImpl.create(request);
    }

    // GET /api/categories
    @GetMapping
    public List<ListCategoryResponse> getAll() {
        return categoryServiceImpl.getAll();
    }

    // GET /api/categories/{id}
    // @PathVariable → URL'deki {id} kısmını parametre olarak alır
    @GetMapping("/{id}")
    public GetByIdCategoryResponse getById(@PathVariable UUID id) {
        return categoryServiceImpl.getById(id);
    }

    // PUT /api/categories/{id}
    @PutMapping("/{id}")
    public UpdatedCategoryResponse update(@PathVariable UUID id,
                                          @Validated @RequestBody UpdateCategoryRequest request) {
        return categoryServiceImpl.update(id, request);
    }

    // DELETE /api/categories/{id}
    // @ResponseStatus(204) → Başarılı silmede body yok, HTTP 204 No Content döner
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable UUID id) {
        categoryServiceImpl.delete(id);
    }
}


// Bu projedeki tum enttiyler icin TUM CRUD islemleri kodlanbmali
// get get by id add update delete islemlerini teker teker yapacagiz.
// Arastirma ODEV ISE SU : JPQL nedir ne icin kullanlir.Bunun kullanimina bakacagiz.
// Kutuphane sistemimizi code-first olsturun. Koda dokulmus halini yapacagiz.,