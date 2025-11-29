package com.example.MocBE.controller;

import com.example.MocBE.dto.request.LocationRequest;
import com.example.MocBE.dto.request.RoleRequest;
import com.example.MocBE.dto.response.*;
import com.example.MocBE.service.LocationService;
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
@RequestMapping("/api/location")
@RequiredArgsConstructor
public class LocationController {

    private final LocationService locationService;

    @GetMapping
    public WrapResponse<PageResponse<LocationResponse>> getLocations(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "50") int size,
            @RequestParam(required = false) String keyword
    ) {
        Pageable pageable = PageRequest.of(page - 1, size, Sort.by(Sort.Direction.DESC, "createdAt"));
        return locationService.getAllLocation(pageable, keyword);
    }

    @PostMapping()
    public ResponseEntity<SuccessResponse> createLocation(@Valid @RequestBody LocationRequest request) throws IOException {
        return locationService.createLocation(request);
    }

    @PutMapping
    public ResponseEntity<SuccessResponse> updateLocation(@RequestBody LocationRequest request) throws IOException {
        return locationService.updateLocation(request);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<SuccessResponse>  deleteLocation(@PathVariable UUID id) {
        return locationService.deleteLocation(id);
    }

    @GetMapping("/{id}/tables")
    public WrapResponse<List<TableResponse>> getTablesByLocation(@PathVariable UUID id) {
        return locationService.getTablesByLocation(id);
    }

    @GetMapping("/{id}/tables/empty")
    public WrapResponse<List<TableResponse>> getTablesEmptyByLocation(@PathVariable UUID id) {
        return locationService.getTablesEmptyByLocation(id);
    }
}
