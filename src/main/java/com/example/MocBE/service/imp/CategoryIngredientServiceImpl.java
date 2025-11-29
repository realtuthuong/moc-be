package com.example.MocBE.service.imp;

import com.example.MocBE.dto.request.CategoryIngredientRequest;
import com.example.MocBE.dto.request.CategoryMenuItemRequest;
import com.example.MocBE.dto.response.*;
import com.example.MocBE.mapper.CategoryIngredientMapper;
import com.example.MocBE.mapper.CategoryMenuItemMapper;
import com.example.MocBE.mapper.WrapResponseFactory;
import com.example.MocBE.model.CategoryIngredient;
import com.example.MocBE.model.CategoryMenuItem;
import com.example.MocBE.repository.CategoryIngredientRepository;
import com.example.MocBE.repository.CategoryMenuItemRepository;
import com.example.MocBE.service.CategoryIngredientService;
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
public class CategoryIngredientServiceImpl implements CategoryIngredientService {
    private final CategoryIngredientMapper categoryIngredientMapper;
    private final CategoryIngredientRepository categoryIngredientRepository;
    private final WrapResponseFactory wrapResponseFactory;

    @Override
    public WrapResponse<PageResponse<CategoryIngredientResponse>> getAllCategoryIngredients(Pageable pageable) {
        try {
            Page<CategoryIngredientResponse> categoryIngredientResponses = categoryIngredientRepository.findAll(pageable)
                    .map(categoryIngredientMapper::toDto);

            PageResponse<CategoryIngredientResponse> pageResponse = PageResponse.<CategoryIngredientResponse>builder()
                    .totalItems(categoryIngredientResponses.getTotalElements())
                    .totalPages(categoryIngredientResponses.getTotalPages())
                    .currentPage(pageable.getPageNumber() + 1)
                    .objectJson(categoryIngredientResponses.getContent())
                    .build();

            return wrapResponseFactory.success(pageResponse, "GET_ALL_CATEGORYINGREDIENT");
        } catch (Exception ex) {
            return wrapResponseFactory.error("GET_ALL_CATEGORYINGREDIENT", null, null, null);
        }
    }

    @Override
    public ResponseEntity<SuccessResponse> createCategoryIngredient(CategoryIngredientRequest request) {
        CategoryIngredient categoryIngredient = new CategoryIngredient();
        categoryIngredient.setName(request.getName());
        categoryIngredientRepository.save(categoryIngredient);
        SuccessResponse response = ResponseUtil.success("Tạo thành công", "/api/category-ingredient");
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @Override
    public ResponseEntity<SuccessResponse> updateCategoryIngredient(CategoryIngredientRequest request) {
        UUID id = request.getId();
        CategoryIngredient categoryIngredient =  categoryIngredientRepository.findById(request.getId())
                .orElseThrow(() -> {
                    return new RuntimeException("Không tìm thấy categoryIngredient với ID: " + id);
                });

        categoryIngredient.setName(request.getName());

        categoryIngredientRepository.save(categoryIngredient);
        SuccessResponse response = ResponseUtil.success(
                "Cập nhật categoryIngredient thành công",
                "/api/category-categoryIngredient/" + id
        );
        return ResponseEntity.ok(response);
    }

    @Override
    public ResponseEntity<SuccessResponse> deleteCategoryIngredient(UUID id) {
        CategoryIngredient categoryIngredient = categoryIngredientRepository.findById(id)
                .orElseThrow(() -> {
                    return new RuntimeException("Không tìm thấy categoryMenuItem có ID: " + id);
                });
        categoryIngredientRepository.delete(categoryIngredient);
        SuccessResponse response = ResponseUtil.success(
                "Xóa categoryIngredient thành công",
                "/api/category-categoryIngredient/" + id
        );
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
}
