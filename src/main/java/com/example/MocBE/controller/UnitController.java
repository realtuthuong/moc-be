package com.example.MocBE.controller;

import com.example.MocBE.dto.UnitDto;
import com.example.MocBE.dto.request.RoleRequest;
import com.example.MocBE.dto.response.PageResponse;
import com.example.MocBE.dto.response.RoleResponse;
import com.example.MocBE.dto.response.SuccessResponse;
import com.example.MocBE.dto.response.WrapResponse;
import com.example.MocBE.repository.UnitRepository;
import com.example.MocBE.service.UnitService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.UUID;

@RestController
@RequestMapping("/api/unit")
@RequiredArgsConstructor
public class UnitController {

    private final UnitService unitService;

    @GetMapping
    public WrapResponse<PageResponse<UnitDto>> getRoles(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "50") int size,
            @RequestParam(required = false) String name
    ) {
        Pageable pageable = PageRequest.of(page - 1, size);
        return unitService.getAllUnits(pageable, name);
    }

    @PostMapping()
    public ResponseEntity<SuccessResponse> createUnit(@Valid @RequestBody UnitDto request) throws IOException {
        return unitService.createUnit(request);
    }

    @PutMapping
    public ResponseEntity<SuccessResponse> updateUnit(@RequestBody UnitDto request) throws IOException {
        return unitService.updateUnit(request);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<SuccessResponse>  deleteRole(@PathVariable UUID id) {
        return unitService.deleteUnit(id);
    }
}
