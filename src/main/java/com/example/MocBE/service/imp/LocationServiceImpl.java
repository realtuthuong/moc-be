package com.example.MocBE.service.imp;

import com.example.MocBE.dto.request.LocationRequest;
import com.example.MocBE.dto.response.*;
import com.example.MocBE.enums.TableStatus;
import com.example.MocBE.mapper.LocationMapper;
import com.example.MocBE.mapper.WrapResponseFactory;
import com.example.MocBE.model.Location;
import com.example.MocBE.repository.LocationRepository;
import com.example.MocBE.repository.TableRepository;
import com.example.MocBE.service.LocationService;
import com.example.MocBE.util.ResponseUtil;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class LocationServiceImpl implements LocationService {

    private static final Logger logger = LoggerFactory.getLogger(LocationServiceImpl.class);

    private final LocationRepository locationRepository;
    private final LocationMapper locationMapper;
    private final TableRepository tableRepository;
    private final WrapResponseFactory wrapResponseFactory;

    @Override
    public WrapResponse<PageResponse<LocationResponse>> getAllLocation(Pageable pageable, String keyword) {
        try {
            logger.info("Lấy danh sách location phân trang");

            Page<Location> locations;

            if (keyword != null && !keyword.isEmpty()) {
                locations = locationRepository.findByNameContainingIgnoreCaseOrAddressContainingIgnoreCase(keyword, keyword, pageable);
            } else {
                locations = locationRepository.findAll(pageable);
            }

            Page<LocationResponse> locationResponses = locations.map(locationMapper::toDto);

            PageResponse<LocationResponse> pageResponse = PageResponse.<LocationResponse>builder()
                    .totalItems(locationResponses.getTotalElements())
                    .totalPages(locationResponses.getTotalPages())
                    .currentPage(pageable.getPageNumber() + 1)
                    .objectJson(locationResponses.getContent())
                    .build();

            return wrapResponseFactory.success(pageResponse, "GET_ALL_LOCATIONS");

        } catch (Exception ex) {
            logger.error("Lỗi khi lấy danh sách location phân trang: ", ex);
            return wrapResponseFactory.error(
                    "GET_ALL_LOCATIONS",
                    "SYSTEM",
                    ex.getMessage(),
                    null
            );
        }
    }

    @Override
    public ResponseEntity<SuccessResponse> createLocation(LocationRequest request) {
        Location location = new Location();
        location.setName(request.getName());
        location.setAddress(request.getAddress());
        location.setOpenTime(request.getOpenTime());
        location.setCloseTime(request.getCloseTime());
        location.setImageUrl(request.getImageUrl());
        location.setStatus(true);
        locationRepository.save(location);
        SuccessResponse response = ResponseUtil.success("Tạo thành công", "/api/location");
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @Override
    public ResponseEntity<SuccessResponse> updateLocation(LocationRequest request) {
        UUID id = request.getId();
        Location location =  locationRepository.findById(request.getId())
                .orElseThrow(() -> {
                    logger.error("Không tìm thấy location để cập nhật: {}", id);
                    return new RuntimeException("Không tìm thấy location với ID: " + id);
                });

        if(request.getStatus() != null){
            location.setStatus(request.getStatus());
        }

        location.setName(request.getName());
        location.setAddress(request.getAddress());
        location.setOpenTime(request.getOpenTime());
        location.setCloseTime(request.getCloseTime());
        location.setImageUrl(request.getImageUrl());

        locationRepository.save(location);
        SuccessResponse response = ResponseUtil.success(
                "Cập nhật location thành công",
                "/api/location/" + id
        );
        return ResponseEntity.ok(response);
    }

    @Override
    public ResponseEntity<SuccessResponse> deleteLocation(UUID id) {
        Location location = locationRepository.findById(id)
                .orElseThrow(() -> {
                    logger.error("Không tìm thấy location có ID: {}", id);
                    return new RuntimeException("Không tìm thấy location có ID: " + id);
                });
        location.setIsDeleted(false);
        locationRepository.save(location);
        SuccessResponse response = ResponseUtil.success(
                "Xóa location thành công",
                "/api/location/" + id
        );
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @Override
    public WrapResponse<List<TableResponse>> getTablesByLocation(UUID locationId) {
        try {
            logger.info("Lấy danh sách bàn theo locationId: {}", locationId);

            // Kiểm tra location tồn tại
            Location location = locationRepository.findById(locationId)
                    .orElseThrow(() -> new RuntimeException("Không tìm thấy location với ID: " + locationId));

            List<TableResponse> tableResponses = tableRepository.findByLocation_Id(locationId)
                    .stream()
                    .map(table -> TableResponse.builder()
                            .id(table.getId())
                            .name(table.getName())
                            .status(table.getStatus())
                            .capacity(table.getCapacity())
                            .locationId(location.getId())
                            .locationName(location.getName())
                            .build())
                    .toList();

            return wrapResponseFactory.success(tableResponses, "GET_TABLES_BY_LOCATION");

        } catch (Exception ex) {
            logger.error("Lỗi khi lấy danh sách bàn theo locationId: ", ex);
            return wrapResponseFactory.error(
                    "GET_TABLES_BY_LOCATION",
                    "SYSTEM",
                    ex.getMessage(),
                    null
            );
        }
    }

    @Override
    public WrapResponse<List<TableResponse>> getTablesEmptyByLocation(UUID locationId) {
        try {
            logger.info("Lấy danh sách bàn TRỐNG theo locationId: {}", locationId);

            Location location = locationRepository.findById(locationId)
                    .orElseThrow(() -> new RuntimeException("Không tìm thấy location với ID: " + locationId));

            List<TableResponse> tableResponses = tableRepository
                    .findByLocation_IdAndStatus(locationId, TableStatus.TRONG)
                    .stream()
                    .map(table -> TableResponse.builder()
                            .id(table.getId())
                            .name(table.getName())
                            .status(table.getStatus())
                            .capacity(table.getCapacity())
                            .locationId(location.getId())
                            .locationName(location.getName())
                            .build())
                    .toList();

            return wrapResponseFactory.success(tableResponses, "GET_EMPTY_TABLES_BY_LOCATION");

        } catch (Exception ex) {
            logger.error("Lỗi khi lấy danh sách bàn trống theo locationId: ", ex);
            return wrapResponseFactory.error(
                    "GET_EMPTY_TABLES_BY_LOCATION",
                    "SYSTEM",
                    ex.getMessage(),
                    null
            );
        }
    }


}
