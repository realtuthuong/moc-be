package com.example.MocBE.service.imp;

import com.example.MocBE.dto.UnitDto;
import com.example.MocBE.dto.request.RoleRequest;
import com.example.MocBE.dto.response.PageResponse;
import com.example.MocBE.dto.response.RoleResponse;
import com.example.MocBE.dto.response.SuccessResponse;
import com.example.MocBE.dto.response.WrapResponse;
import com.example.MocBE.mapper.UnitMapper;
import com.example.MocBE.mapper.WrapResponseFactory;
import com.example.MocBE.model.Role;
import com.example.MocBE.model.Unit;
import com.example.MocBE.repository.UnitRepository;
import com.example.MocBE.service.UnitService;
import com.example.MocBE.util.ResponseUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UnitServiceImpl implements UnitService {

    private final UnitRepository unitRepository;
    private final UnitMapper unitMapper;
    private final WrapResponseFactory wrapResponseFactory;

    @Override
    public WrapResponse<PageResponse<UnitDto>> getAllUnits(Pageable pageable, String name) {
        try {
            Page<UnitDto> unitDto;

            if (name != null && !name.isBlank()) {
                unitDto = unitRepository.findByNameContainingIgnoreCase(name, pageable)
                        .map(unitMapper::toDto);
            } else {
                unitDto = unitRepository.findAll(pageable)
                        .map(unitMapper::toDto);
            }

            PageResponse<UnitDto> pageResponse = PageResponse.<UnitDto>builder()
                    .totalItems(unitDto.getTotalElements())
                    .totalPages(unitDto.getTotalPages())
                    .currentPage(pageable.getPageNumber() + 1)
                    .objectJson(unitDto.getContent())
                    .build();

            return wrapResponseFactory.success(pageResponse, "GET_ALL_UNIT");
        } catch (Exception ex) {
            return wrapResponseFactory.error("GET_ALL_UNIT", null, null, null);
        }
    }

    @Override
    public ResponseEntity<SuccessResponse> createUnit(UnitDto request) {
        Unit unit = new Unit();
        unit.setName(request.getName());
        unitRepository.save(unit);
        SuccessResponse response = ResponseUtil.success("Tạo thành công", "/api/unit");
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @Override
    public ResponseEntity<SuccessResponse> updateUnit(UnitDto request) {
        UUID id = request.getId();
        Unit unit =  unitRepository.findById(request.getId())
                .orElseThrow(() -> {
                    return new RuntimeException("Không tìm thấy unit với ID: " + id);
                });

        unit.setName(request.getName());
        unitRepository.save(unit);
        SuccessResponse response = ResponseUtil.success(
                "Cập nhật unit thành công",
                "/api/unit/" + id
        );
        return ResponseEntity.ok(response);
    }

    @Override
    public ResponseEntity<SuccessResponse> deleteUnit(UUID id) {
        Unit unit = unitRepository.findById(id)
                .orElseThrow(() -> {
                    return new RuntimeException("Không tìm thấy unit có ID: " + id);
                });
        unitRepository.delete(unit);
        SuccessResponse response = ResponseUtil.success(
                "Xóa unit thành công",
                "/api/unit/" + id
        );
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
}
