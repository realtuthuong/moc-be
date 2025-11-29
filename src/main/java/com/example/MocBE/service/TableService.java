package com.example.MocBE.service;

import com.example.MocBE.dto.request.TableFilterRequest;
import com.example.MocBE.dto.request.TableRequest;
import com.example.MocBE.dto.response.*;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

import java.util.UUID;

public interface TableService {
    WrapResponse<PageResponse<TableResponse>> getAllTables(TableFilterRequest filterRequest, Pageable pageable);
    ResponseEntity<SuccessResponse> createTable(TableRequest request);
    ResponseEntity<SuccessResponse> updateTable(TableRequest request);
    ResponseEntity<SuccessResponse> deleteTable(UUID id);
    WrapResponse<PageResponse<TableResponse>> getTablesByLocation(UUID locationId, Pageable pageable);
}
