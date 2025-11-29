package com.example.MocBE.service.imp;

import com.example.MocBE.dto.request.CategoryMenuItemRequest;
import com.example.MocBE.dto.response.*;
import com.example.MocBE.mapper.CategoryMenuItemMapper;
import com.example.MocBE.mapper.WrapResponseFactory;
import com.example.MocBE.model.CategoryMenuItem;
import com.example.MocBE.model.Location;
import com.example.MocBE.repository.CategoryMenuItemRepository;
import com.example.MocBE.repository.LocationRepository;
import com.example.MocBE.service.CategoryMenuItemService;
import com.example.MocBE.util.ResponseUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CategoryMenuItemServiceImpl implements CategoryMenuItemService {

    private final CategoryMenuItemMapper categoryMenuItemMapper;
    private final CategoryMenuItemRepository categoryMenuItemRepository;
    private final WrapResponseFactory wrapResponseFactory;
    private final LocationRepository locationRepository;

    @Override
    public WrapResponse<PageResponse<CategoryMenuItemResponse>> getAllCategoryMenuItems(Pageable pageable) {
        try {
            Page<CategoryMenuItemResponse> categoryMenuItemResponses = categoryMenuItemRepository.findAll(pageable)
                    .map(categoryMenuItemMapper::toDto);

            PageResponse<CategoryMenuItemResponse> pageResponse = PageResponse.<CategoryMenuItemResponse>builder()
                    .totalItems(categoryMenuItemResponses.getTotalElements())
                    .totalPages(categoryMenuItemResponses.getTotalPages())
                    .currentPage(pageable.getPageNumber() + 1)
                    .objectJson(categoryMenuItemResponses.getContent())
                    .build();

            return wrapResponseFactory.success(pageResponse, "GET_ALL_CategoryMenuItemRes");
        } catch (Exception ex) {
            return wrapResponseFactory.error("GET_ALL_CategoryMenuItemRes", null, null, null);
        }
    }

    @Override
    public WrapResponse<PageResponse<CategoryMenuItemResponse>> getCategoryMenuItemsByLocation(UUID locationId, Pageable pageable) {
        try {
            Page<CategoryMenuItemResponse> categoryMenuItemResponses = categoryMenuItemRepository
                    .findAll((root, query, cb) -> cb.equal(root.get("location").get("id"), locationId), pageable)
                    .map(categoryMenuItemMapper::toDto);

            PageResponse<CategoryMenuItemResponse> pageResponse = PageResponse.<CategoryMenuItemResponse>builder()
                    .totalItems(categoryMenuItemResponses.getTotalElements())
                    .totalPages(categoryMenuItemResponses.getTotalPages())
                    .currentPage(pageable.getPageNumber() + 1)
                    .objectJson(categoryMenuItemResponses.getContent())
                    .build();

            return wrapResponseFactory.success(pageResponse, "GET_CategoryMenuItem_BY_LOCATION");
        } catch (Exception ex) {
            return wrapResponseFactory.error("GET_CategoryMenuItem_BY_LOCATION", null, null, null);
        }
    }

    @Override
    public ResponseEntity<SuccessResponse> createCategoryMenuItem(CategoryMenuItemRequest request) {

        CategoryMenuItem categoryMenuItem = new CategoryMenuItem();
        categoryMenuItem.setName(request.getName());

        categoryMenuItemRepository.save(categoryMenuItem);

        SuccessResponse response = ResponseUtil.success("Tạo thành công", "/api/category-menu-items");
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }


    @Override
    public ResponseEntity<SuccessResponse> updateCategoryMenuItem(CategoryMenuItemRequest request) {
        UUID id = request.getId();
        CategoryMenuItem categoryMenuItem =  categoryMenuItemRepository.findById(request.getId())
                .orElseThrow(() -> {
                    return new RuntimeException("Không tìm thấy categoryMenuItem với ID: " + id);
                });

        categoryMenuItem.setName(request.getName());

        categoryMenuItemRepository.save(categoryMenuItem);
        SuccessResponse response = ResponseUtil.success(
                "Cập nhật categoryMenuItem thành công",
                "/api/category-menu-items/" + id
        );
        return ResponseEntity.ok(response);
    }

    @Override
    public ResponseEntity<SuccessResponse> deleteCategoryMenuItem(UUID id) {
        CategoryMenuItem categoryMenuItem = categoryMenuItemRepository.findById(id)
                .orElseThrow(() -> {
                    return new RuntimeException("Không tìm thấy categoryMenuItem có ID: " + id);
                });
        categoryMenuItemRepository.delete(categoryMenuItem);
        SuccessResponse response = ResponseUtil.success(
                "Xóa categoryMenuItem thành công",
                "/api/category-menu-items/" + id
        );
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }


}
