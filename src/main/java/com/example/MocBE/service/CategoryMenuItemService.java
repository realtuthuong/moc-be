package com.example.MocBE.service;

import com.example.MocBE.dto.request.CategoryMenuItemRequest;
import com.example.MocBE.dto.response.*;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

import java.util.UUID;

public interface CategoryMenuItemService {
    WrapResponse<PageResponse<CategoryMenuItemResponse>> getAllCategoryMenuItems(Pageable pageable);
    ResponseEntity<SuccessResponse> updateCategoryMenuItem(CategoryMenuItemRequest request);
    ResponseEntity<SuccessResponse> deleteCategoryMenuItem(UUID id);
    WrapResponse<PageResponse<CategoryMenuItemResponse>> getCategoryMenuItemsByLocation(UUID locationId, Pageable pageable);
    ResponseEntity<SuccessResponse> createCategoryMenuItem(CategoryMenuItemRequest request);
}
