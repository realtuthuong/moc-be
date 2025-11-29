package com.example.MocBE.controller;

import com.example.MocBE.dto.request.TableFilterRequest;
import com.example.MocBE.dto.request.TableRequest;
import com.example.MocBE.dto.response.*;
import com.example.MocBE.service.TableService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;


@RestController
@RequestMapping("/api/table")
@RequiredArgsConstructor
public class TableController {

    private final TableService tableService;

    @GetMapping
    public WrapResponse<PageResponse<TableResponse>> getAllTables(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "50") int size,
            TableFilterRequest filterRequest
    ) {
        Pageable pageable = PageRequest.of(page - 1, size, Sort.by(Sort.Direction.DESC, "createdAt"));
        return tableService.getAllTables(filterRequest, pageable);
    }

    @PostMapping
    public ResponseEntity<SuccessResponse> createTable(@Valid @RequestBody TableRequest request) {
        return tableService.createTable(request);
    }

    @PutMapping
    public ResponseEntity<SuccessResponse> updateTable(@Valid @RequestBody TableRequest request) {
        return tableService.updateTable(request);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<SuccessResponse> deleteTable(@PathVariable UUID id) {
        return tableService.deleteTable(id);
    }

    @GetMapping("/by-location/{locationId}")
    public WrapResponse<PageResponse<TableResponse>> getTablesByLocation(
            @PathVariable UUID locationId,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "50") int size
    ) {
        Pageable pageable = PageRequest.of(page - 1, size, Sort.by(Sort.Direction.DESC, "createdAt"));
        return tableService.getTablesByLocation(locationId, pageable);
    }
}
