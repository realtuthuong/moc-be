package com.example.MocBE.service;

import com.example.MocBE.dto.IngredientDto;
import com.example.MocBE.dto.request.IngredientFilterRequest;
import com.example.MocBE.dto.request.IngredientRequest;
import com.example.MocBE.dto.response.IngredientResponse;
import com.example.MocBE.dto.response.PageResponse;
import com.example.MocBE.dto.response.SuccessResponse;
import com.example.MocBE.dto.response.WrapResponse;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

import java.io.IOException;
import java.util.UUID;

public interface IngredientService {
    WrapResponse<PageResponse<IngredientResponse>> getAllIngredients(IngredientFilterRequest filterRequest, Pageable pageable);
    ResponseEntity<SuccessResponse> deleteIngredient(UUID id);
    ResponseEntity<SuccessResponse> updateIngredient(IngredientDto request) throws IOException;
    ResponseEntity<SuccessResponse> createIngredient(IngredientRequest request) throws IOException;
    WrapResponse<PageResponse<IngredientResponse>> getIngredientsByLocation(UUID locationId, IngredientFilterRequest filterRequest, Pageable pageable);

}
