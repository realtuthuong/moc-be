package com.example.MocBE.service;

import com.example.MocBE.dto.request.CategoryIngredientRequest;
import com.example.MocBE.dto.request.CategoryMenuItemRequest;
import com.example.MocBE.dto.response.*;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

import java.util.UUID;

public interface CategoryIngredientService {
    WrapResponse<PageResponse<CategoryIngredientResponse>> getAllCategoryIngredients(Pageable pageable);
    ResponseEntity<SuccessResponse> createCategoryIngredient(CategoryIngredientRequest request);
    ResponseEntity<SuccessResponse> updateCategoryIngredient(CategoryIngredientRequest request);
    ResponseEntity<SuccessResponse> deleteCategoryIngredient(UUID id);
}
