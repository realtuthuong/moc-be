package com.example.MocBE.controller;

import com.example.MocBE.dto.request.MenuItemFilterRequest;
import com.example.MocBE.dto.request.MenuItemRequest;
import com.example.MocBE.dto.response.*;
import com.example.MocBE.service.MenuItemService;
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
@RequestMapping("/api/menu-items")
@RequiredArgsConstructor
public class MenuItemController {

    private final MenuItemService menuItemService;

    @GetMapping
    public WrapResponse<PageResponse<MenuItemResponse>> getMenuItems(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "20") int size,
            MenuItemFilterRequest filterRequest
    ) {
        Pageable pageable = PageRequest.of(page - 1, size, Sort.by(Sort.Direction.DESC, "createdAt"));
        return menuItemService.getAllMenuItems(filterRequest, pageable);
    }

    @GetMapping("/category/{id}")
    public WrapResponse<PageResponse<MenuItemResponse>> getMenuItemsByCategory(
            @PathVariable UUID id,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "20") int size
    ) {
        Pageable pageable = PageRequest.of(page - 1, size, Sort.by(Sort.Direction.DESC, "createdAt"));
        return menuItemService.getMenuItemsByCategory(id, pageable);
    }

    @GetMapping("/{id}")
    public MenuItemResponse getMenuItemById(@PathVariable UUID id) {
        return menuItemService.getMenuItemById(id);
    }

    @PostMapping
    public ResponseEntity<SuccessResponse> createMenuItem(
            @Valid @ModelAttribute MenuItemRequest request
    ) throws IOException {
        return menuItemService.createMenuItem(request);
    }

    @PutMapping
    public ResponseEntity<SuccessResponse> updateMenuItem(@Valid @ModelAttribute MenuItemRequest request) throws IOException {
        return menuItemService.updateMenuItem(request);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<SuccessResponse> deleteMenuItem(@PathVariable UUID id) {
        return menuItemService.deleteMenuItem(id);
    }

    @GetMapping("/total")
    public WrapResponse<TotalMenuItemResponse> getTotalMenuItems() {
        return menuItemService.getTotalMenuItems();
    }

    @GetMapping("/top-selling")
    public WrapResponse<List<TopSellingMenuItemResponse>> getTopSellingMenuItems() {
        return menuItemService.getTopSellingMenuItems();
    }

    @GetMapping("/by-location/{locationId}")
    public WrapResponse<PageResponse<MenuItemResponse>> getMenuItemsByLocation(
            @PathVariable UUID locationId,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "20") int size,
            MenuItemFilterRequest filterRequest
    ) {
        Pageable pageable = PageRequest.of(page - 1, size, Sort.by(Sort.Direction.DESC, "createdAt"));
        return menuItemService.getMenuItemsByLocation(locationId, filterRequest, pageable);
    }

}
