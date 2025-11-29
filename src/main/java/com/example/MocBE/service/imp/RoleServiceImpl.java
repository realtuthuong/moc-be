package com.example.MocBE.service.imp;

import com.example.MocBE.dto.request.RoleRequest;
import com.example.MocBE.dto.response.*;
import com.example.MocBE.mapper.RoleMapper;
import com.example.MocBE.mapper.WrapResponseFactory;
import com.example.MocBE.model.Role;
import com.example.MocBE.repository.RoleRepository;
import com.example.MocBE.service.RoleService;
import com.example.MocBE.util.ResponseUtil;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RoleServiceImpl implements RoleService {

    private static final Logger logger = LoggerFactory.getLogger(RoleServiceImpl.class);

    private final RoleRepository roleRepository;
    private final RoleMapper roleMapper;
    private final WrapResponseFactory wrapResponseFactory;

    @Override
    public WrapResponse<PageResponse<RoleResponse>> getAllRoles(Pageable pageable, String roleName) {
        try {
            Page<RoleResponse> roles;

            if (roleName != null && !roleName.isBlank()) {
                roles = roleRepository.findByNameContainingIgnoreCase(roleName, pageable)
                        .map(roleMapper::toDto);
            } else {
                roles = roleRepository.findAll(pageable)
                        .map(roleMapper::toDto);
            }

            PageResponse<RoleResponse> pageResponse = PageResponse.<RoleResponse>builder()
                    .totalItems(roles.getTotalElements())
                    .totalPages(roles.getTotalPages())
                    .currentPage(pageable.getPageNumber() + 1)
                    .objectJson(roles.getContent())
                    .build();

            return wrapResponseFactory.success(pageResponse, "GET_ALL_ROLE");
        } catch (Exception ex) {
            logger.error("Lỗi khi lấy danh sách role phân trang: ", ex);
            return wrapResponseFactory.error("GET_ALL_ROLE", null, null, null);
        }
    }

    @Override
    public ResponseEntity<SuccessResponse> createRole(RoleRequest request) {
        Role role = new Role();
        role.setName(request.getName());
        role.setDescription(request.getDescription());
        roleRepository.save(role);
        SuccessResponse response = ResponseUtil.success("Tạo thành công", "/api/role");
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @Override
    public ResponseEntity<SuccessResponse> updateRole(RoleRequest request) {
        UUID id = request.getId();
        Role role =  roleRepository.findById(request.getId())
                .orElseThrow(() -> {
                    logger.error("Không tìm thấy role để cập nhật: {}", id);
                    return new RuntimeException("Không tìm thấy role với ID: " + id);
                });

        role.setName(request.getName());
        role.setDescription(request.getDescription());

        roleRepository.save(role);
        SuccessResponse response = ResponseUtil.success(
                "Cập nhật role thành công",
                "/api/role/" + id
        );
        return ResponseEntity.ok(response);
    }

    @Override
    public ResponseEntity<SuccessResponse> deleteRole(UUID id) {
        Role role = roleRepository.findById(id)
                .orElseThrow(() -> {
                    logger.error("Không tìm thấy role có ID: {}", id);
                    return new RuntimeException("Không tìm thấy role có ID: " + id);
                });
        roleRepository.delete(role);
        SuccessResponse response = ResponseUtil.success(
                "Xóa role thành công",
                "/api/role/" + id
        );
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
}
