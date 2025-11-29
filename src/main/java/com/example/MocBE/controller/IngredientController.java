package com.example.MocBE.controller;

import com.example.MocBE.dto.IngredientDto;
import com.example.MocBE.dto.request.*;
import com.example.MocBE.dto.response.*;
import com.example.MocBE.service.IngredientService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.UUID;

@RestController
@RequestMapping("/api/ingredient")
@RequiredArgsConstructor
public class IngredientController {

    private final IngredientService ingredientService;

    @GetMapping
    public WrapResponse<PageResponse<IngredientResponse>> getIngredients(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "50") int size,
            IngredientFilterRequest filterRequest
    ) {
        Pageable pageable = PageRequest.of(page - 1, size, Sort.by(Sort.Direction.DESC, "createdAt"));
        return ingredientService.getAllIngredients(filterRequest, pageable);
    }

    @GetMapping("/by-location/{locationId}")
    public WrapResponse<PageResponse<IngredientResponse>> getIngredientsByLocation(
            @PathVariable UUID locationId,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "50") int size,
            IngredientFilterRequest filterRequest
    ) {
        Pageable pageable = PageRequest.of(page - 1, size, Sort.by(Sort.Direction.DESC, "createdAt"));
        return ingredientService.getIngredientsByLocation(locationId, filterRequest, pageable);
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<SuccessResponse> deleteIngredient(@PathVariable UUID id) {
        return ingredientService.deleteIngredient(id);
    }

    @PutMapping
    public ResponseEntity<SuccessResponse> updateIngredient(@RequestBody IngredientDto request) throws IOException {
        return ingredientService.updateIngredient(request);
    }

    @PostMapping()
    public ResponseEntity<SuccessResponse> createIngredient(@Valid @RequestBody IngredientRequest request) throws IOException {
        return ingredientService.createIngredient(request);
    }
}
