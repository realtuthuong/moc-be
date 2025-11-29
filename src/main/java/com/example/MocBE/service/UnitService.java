package com.example.MocBE.service;

import com.example.MocBE.dto.UnitDto; 
import com.example.MocBE.dto.response.*;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

import java.util.UUID;

public interface UnitService {
    WrapResponse<PageResponse<UnitDto>> getAllUnits(Pageable pageable, String name);
    ResponseEntity<SuccessResponse> createUnit(UnitDto request);
    ResponseEntity<SuccessResponse> updateUnit(UnitDto request);
    ResponseEntity<SuccessResponse> deleteUnit(UUID id);
}
