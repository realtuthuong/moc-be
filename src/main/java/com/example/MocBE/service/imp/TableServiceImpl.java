package com.example.MocBE.service.imp;

import com.example.MocBE.dto.request.TableRequest;
import com.example.MocBE.dto.request.TableFilterRequest;
import com.example.MocBE.dto.response.*;
import com.example.MocBE.mapper.WrapResponseFactory;
import com.example.MocBE.model.Location;
import com.example.MocBE.model.Table;
import com.example.MocBE.repository.LocationRepository;
import com.example.MocBE.repository.TableRepository;
import com.example.MocBE.repository.spec.TableSpecification;
import com.example.MocBE.service.TableService;
import com.example.MocBE.util.ResponseUtil;
import jakarta.transaction.Transactional;
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
public class TableServiceImpl implements TableService {

    private static final Logger logger = LoggerFactory.getLogger(TableServiceImpl.class);

    private final TableRepository tableRepository;
    private final LocationRepository locationRepository;
    private final WrapResponseFactory wrapResponseFactory;

    @Override
    public WrapResponse<PageResponse<TableResponse>> getAllTables(TableFilterRequest filterRequest, Pageable pageable) {
        try {
            var spec = TableSpecification.filter(filterRequest);

            Page<TableResponse> page = tableRepository.findAll(spec, pageable)
                    .map(t -> TableResponse.builder()
                            .id(t.getId())
                            .name(t.getName())
                            .status(t.getStatus())
                            .capacity(t.getCapacity())
                            .createdAt(String.valueOf(t.getCreatedAt()))
                            .updatedAt(String.valueOf((t.getUpdatedAt())))
                            .locationId(t.getLocation().getId())
                            .locationName(t.getLocation().getName())
                            .build()
                    );

            PageResponse<TableResponse> response = PageResponse.<TableResponse>builder()
                    .totalItems(page.getTotalElements())
                    .totalPages(page.getTotalPages())
                    .currentPage(pageable.getPageNumber() + 1)
                    .objectJson(page.getContent())
                    .build();

            return wrapResponseFactory.success(response, "GET_ALL_TABLES");

        } catch (Exception e) {
            logger.error("Lỗi khi lấy danh sách bàn", e);
            return wrapResponseFactory.error("GET_ALL_TABLES", "SYSTEM", e.getMessage(), null);
        }
    }

    @Override
    @Transactional
    public ResponseEntity<SuccessResponse> createTable(TableRequest request) {
        Location location = locationRepository.findById(request.getLocationId())
                .orElseThrow(() -> new RuntimeException("Không tìm thấy locationId: " + request.getLocationId()));

        if (tableRepository.existsByNameAndLocationIdAndIsDeletedFalse(request.getName(), location.getId())) {
            throw new RuntimeException("Tên bàn đã tồn tại trong location này!");
        }

        Table table = new Table();
        table.setName(request.getName());
        table.setStatus(request.getStatus());
        table.setCapacity(request.getCapacity());
        table.setLocation(location);
        table.setIsDeleted(false);

        tableRepository.save(table);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ResponseUtil.success("Tạo bàn thành công", "/api/table"));
    }

    @Override
    @Transactional
    public ResponseEntity<SuccessResponse> updateTable(TableRequest request) {
        if (request.getId() == null) {
            throw new RuntimeException("Id không được null khi update!");
        }

        Table table = tableRepository.findById(request.getId())
                .orElseThrow(() -> new RuntimeException("Không tìm thấy tableId: " + request.getId()));

        Location location = locationRepository.findById(request.getLocationId())
                .orElseThrow(() -> new RuntimeException("Không tìm thấy locationId: " + request.getLocationId()));

        table.setName(request.getName());
        table.setStatus(request.getStatus());
        table.setCapacity(request.getCapacity());
        table.setLocation(location);

        tableRepository.save(table);

        return ResponseEntity.ok(ResponseUtil.success("Cập nhật bàn thành công", "/api/table/" + request.getId()));
    }

    @Override
    @Transactional
    public ResponseEntity<SuccessResponse> deleteTable(UUID id) {
        Table table = tableRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy tableId: " + id));

        table.setIsDeleted(true);
        tableRepository.save(table);

        return ResponseEntity.ok(ResponseUtil.success("Xóa bàn thành công", "/api/table/" + id));
    }

    @Override
    public WrapResponse<PageResponse<TableResponse>> getTablesByLocation(UUID locationId, Pageable pageable) {
        try {
            Page<TableResponse> page = tableRepository.findAllByLocationIdAndIsDeletedFalse(locationId, pageable)
                    .map(t -> TableResponse.builder()
                            .id(t.getId())
                            .name(t.getName())
                            .status(t.getStatus())
                            .capacity(t.getCapacity())
                            .createdAt(String.valueOf(t.getCreatedAt()))
                            .updatedAt(String.valueOf((t.getUpdatedAt())))
                            .locationId(t.getLocation().getId())
                            .locationName(t.getLocation().getName())
                            .build()
                    );

            PageResponse<TableResponse> response = PageResponse.<TableResponse>builder()
                    .totalItems(page.getTotalElements())
                    .totalPages(page.getTotalPages())
                    .currentPage(pageable.getPageNumber() + 1)
                    .objectJson(page.getContent())
                    .build();

            return wrapResponseFactory.success(response, "GET_TABLES_BY_LOCATION");

        } catch (Exception e) {
            logger.error("Lỗi khi lấy danh sách bàn theo locationId {}", locationId, e);
            return wrapResponseFactory.error("GET_TABLES_BY_LOCATION", "SYSTEM", e.getMessage(), null);
        }
    }

}
