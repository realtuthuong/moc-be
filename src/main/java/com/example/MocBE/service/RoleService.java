package com.example.MocBE.service;

import com.example.MocBE.dto.request.RoleRequest;
import com.example.MocBE.dto.response.*;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

import java.util.UUID;

public interface RoleService {
    WrapResponse<PageResponse<RoleResponse>> getAllRoles(Pageable pageable, String roleName);
    ResponseEntity<SuccessResponse> createRole(RoleRequest request);
    ResponseEntity<SuccessResponse> updateRole(RoleRequest request);
    ResponseEntity<SuccessResponse> deleteRole(UUID id);
}
