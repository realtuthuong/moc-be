package com.example.MocBE.service;

import com.example.MocBE.dto.request.CreateAccountRequest;
import com.example.MocBE.dto.request.LocationRequest;
import com.example.MocBE.dto.request.RoleRequest;
import com.example.MocBE.dto.request.UpdateAccountRequest;
import com.example.MocBE.dto.response.*;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

public interface LocationService {
    WrapResponse<PageResponse<LocationResponse>> getAllLocation(Pageable pageable, String keyword);
    ResponseEntity<SuccessResponse> createLocation(LocationRequest request);
    ResponseEntity<SuccessResponse> updateLocation(LocationRequest request);
    ResponseEntity<SuccessResponse> deleteLocation(UUID id);
    WrapResponse<List<TableResponse>> getTablesByLocation(UUID locationId);
    WrapResponse<List<TableResponse>> getTablesEmptyByLocation(UUID locationId);
}
