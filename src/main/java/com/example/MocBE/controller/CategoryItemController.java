package com.example.MocBE.controller;

import com.example.MocBE.dto.request.CategoryMenuItemRequest;
import com.example.MocBE.dto.request.RoleRequest;
import com.example.MocBE.dto.response.*;
import com.example.MocBE.service.CategoryMenuItemService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/category-menu-items")
@RequiredArgsConstructor
public class CategoryItemController {

    private final CategoryMenuItemService categoryMenuItemService;

    @GetMapping
    public WrapResponse<PageResponse<CategoryMenuItemResponse>> getCategoryMenuItems(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "50") int size
    ) {
        Pageable pageable = PageRequest.of(page - 1, size, Sort.by(Sort.Direction.DESC, "createdAt"));
        return categoryMenuItemService.getAllCategoryMenuItems(pageable);
    }

    @GetMapping("/by-location/{locationId}")
    public WrapResponse<PageResponse<CategoryMenuItemResponse>> getCategoryMenuItemsByLocation(
            @PathVariable UUID locationId,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "50") int size
    ) {
        Pageable pageable = PageRequest.of(page - 1, size, Sort.by(Sort.Direction.DESC, "createdAt"));
        return categoryMenuItemService.getCategoryMenuItemsByLocation(locationId, pageable);
    }


    @PostMapping
    public ResponseEntity<SuccessResponse> createCategoryMenuItem(
            @Valid @RequestBody CategoryMenuItemRequest request
    ) {
        return categoryMenuItemService.createCategoryMenuItem(request);
    }


    @PutMapping
    public ResponseEntity<SuccessResponse> updateCategoryMenuItem(@RequestBody CategoryMenuItemRequest request) throws IOException {
        return categoryMenuItemService.updateCategoryMenuItem(request);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<SuccessResponse>  deleteCategoryMenuItem(@PathVariable UUID id) {
        return categoryMenuItemService.deleteCategoryMenuItem(id);
    }

}
