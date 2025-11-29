package com.example.MocBE.controller;

import com.example.MocBE.dto.request.RoleRequest;
import com.example.MocBE.dto.response.*;
import com.example.MocBE.service.RoleService;
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
@RequestMapping("/api/role")
@RequiredArgsConstructor
public class RoleController {

    private final RoleService roleService;

    @GetMapping
    public WrapResponse<PageResponse<RoleResponse>> getRoles(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "50") int size,
            @RequestParam(required = false) String roleName
    ) {
        Pageable pageable = PageRequest.of(page - 1, size, Sort.by(Sort.Direction.DESC, "createdAt"));
        return roleService.getAllRoles(pageable, roleName);
    }

    @PostMapping()
    public ResponseEntity<SuccessResponse> createRole(@Valid @RequestBody RoleRequest request) throws IOException {
        return roleService.createRole(request);
    }

    @PutMapping
    public ResponseEntity<SuccessResponse> updateRole(@RequestBody RoleRequest request) throws IOException {
        return roleService.updateRole(request);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<SuccessResponse>  deleteRole(@PathVariable UUID id) {
        return roleService.deleteRole(id);
    }

}
