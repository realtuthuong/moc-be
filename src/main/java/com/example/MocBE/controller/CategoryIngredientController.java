package com.example.MocBE.controller;

import com.example.MocBE.dto.request.CategoryIngredientRequest;
import com.example.MocBE.dto.request.CategoryMenuItemRequest;
import com.example.MocBE.dto.response.*;
import com.example.MocBE.service.CategoryIngredientService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.UUID;

@RestController
@RequestMapping("/api/category-ingredient")
@RequiredArgsConstructor
public class CategoryIngredientController {

    private final CategoryIngredientService categoryIngredientService;

    @GetMapping
    public WrapResponse<PageResponse<CategoryIngredientResponse>> getCategoryIngredients(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "50") int size
    ) {
        Pageable pageable = PageRequest.of(page - 1, size, Sort.by(Sort.Direction.DESC, "createdAt"));
        return categoryIngredientService.getAllCategoryIngredients(pageable);
    }

    @PostMapping()
    public ResponseEntity<SuccessResponse> createCategoryIngredient(@Valid @RequestBody CategoryIngredientRequest request) throws IOException {
        return categoryIngredientService.createCategoryIngredient(request);
    }

    @PutMapping
    public ResponseEntity<SuccessResponse> updateCategoryIngredient(@RequestBody CategoryIngredientRequest request) throws IOException {
        return categoryIngredientService.updateCategoryIngredient(request);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<SuccessResponse>  deleteCategoryIngredient(@PathVariable UUID id) {
        return categoryIngredientService.deleteCategoryIngredient(id);
    }

}
