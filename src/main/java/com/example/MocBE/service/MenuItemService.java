package com.example.MocBE.service;

import com.example.MocBE.dto.request.MenuItemFilterRequest;
import com.example.MocBE.dto.request.MenuItemRequest;
import com.example.MocBE.dto.response.*;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;


import java.io.IOException;
import java.util.List;
import java.util.UUID;


public interface MenuItemService {
    WrapResponse<PageResponse<MenuItemResponse>> getAllMenuItems(MenuItemFilterRequest filter, Pageable pageable);
    MenuItemResponse getMenuItemById(UUID id);
    WrapResponse<PageResponse<MenuItemResponse>> getMenuItemsByCategory(UUID categoryId, Pageable pageable);
    ResponseEntity<SuccessResponse> createMenuItem(MenuItemRequest request) throws IOException;
    ResponseEntity<SuccessResponse> updateMenuItem(MenuItemRequest request) throws IOException;
    ResponseEntity<SuccessResponse> deleteMenuItem(UUID id);
    WrapResponse<TotalMenuItemResponse> getTotalMenuItems();
    WrapResponse<List<TopSellingMenuItemResponse>> getTopSellingMenuItems();
    WrapResponse<PageResponse<MenuItemResponse>> getMenuItemsByLocation(UUID locationId, MenuItemFilterRequest filter, Pageable pageable);


}
