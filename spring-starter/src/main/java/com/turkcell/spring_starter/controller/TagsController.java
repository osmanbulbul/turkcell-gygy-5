package com.turkcell.spring_starter.controller;

import java.util.List;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import com.turkcell.spring_starter.dto.CreateTagRequest;
import com.turkcell.spring_starter.dto.CreatedTagResponse;
import com.turkcell.spring_starter.dto.GetByIdTagResponse;
import com.turkcell.spring_starter.dto.ListTagResponse;
import com.turkcell.spring_starter.dto.UpdateTagRequest;
import com.turkcell.spring_starter.dto.UpdatedTagResponse;
import com.turkcell.spring_starter.service.TagServiceImpl;

@RestController
@RequestMapping("/api/tags")
public class TagsController {

    private final TagServiceImpl tagServiceImpl;

    public TagsController(TagServiceImpl tagServiceImpl) {
        this.tagServiceImpl = tagServiceImpl;
    }

    // POST /api/tags
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CreatedTagResponse create(@Validated @RequestBody CreateTagRequest request) {
        return tagServiceImpl.create(request);
    }

    // GET /api/tags
    @GetMapping
    public List<ListTagResponse> getAll() {
        return tagServiceImpl.getAll();
    }

    // GET /api/tags/{id}
    @GetMapping("/{id}")
    public GetByIdTagResponse getById(@PathVariable UUID id) {
        return tagServiceImpl.getById(id);
    }

    // PUT /api/tags/{id}
    @PutMapping("/{id}")
    public UpdatedTagResponse update(@PathVariable UUID id,
                                     @Validated @RequestBody UpdateTagRequest request) {
        return tagServiceImpl.update(id, request);
    }

    // DELETE /api/tags/{id}
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable UUID id) {
        tagServiceImpl.delete(id);
    }
}