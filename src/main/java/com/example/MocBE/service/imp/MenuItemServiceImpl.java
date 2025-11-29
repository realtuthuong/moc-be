package com.example.MocBE.service.imp;

import com.example.MocBE.dto.request.MenuItemFilterRequest;
import com.example.MocBE.dto.request.MenuItemRequest;
import com.example.MocBE.dto.response.*;
import com.example.MocBE.enums.MenuItemStatus;
import com.example.MocBE.mapper.MenuItemMapper;
import com.example.MocBE.mapper.WrapResponseFactory;
import com.example.MocBE.model.CategoryMenuItem;
import com.example.MocBE.model.Location;
import com.example.MocBE.model.MenuItem;
import com.example.MocBE.model.MenuItemImages;
import com.example.MocBE.repository.CategoryMenuItemRepository;
import com.example.MocBE.repository.LocationRepository;
import com.example.MocBE.repository.MenuItemRepository;
import com.example.MocBE.repository.OrderDetailRepository;
import com.example.MocBE.repository.spec.MenuItemSpecification;
import com.example.MocBE.service.CloudinaryService;
import com.example.MocBE.service.MenuItemService;
import com.example.MocBE.util.ResponseUtil;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MenuItemServiceImpl implements MenuItemService {

    private static final Logger logger = LoggerFactory.getLogger(MenuItemServiceImpl.class);

    private final MenuItemRepository menuItemRepository;
    private final CategoryMenuItemRepository categoryMenuItemRepository;
    private final MenuItemMapper menuItemMapper;
    private final WrapResponseFactory wrapResponseFactory;
    private final CloudinaryService cloudinaryService;
    private final OrderDetailRepository orderDetailRepository;
    private final LocationRepository locationRepository;

    @Override
    public WrapResponse<PageResponse<MenuItemResponse>> getAllMenuItems(MenuItemFilterRequest filter, Pageable pageable) {
        logger.info("Bắt đầu lấy danh sách món ăn có phân trang + lọc");
        try {
            Specification<MenuItem> spec = Specification
                    .where(MenuItemSpecification.nameContains(filter.getName()))
                    .and(MenuItemSpecification.categoryEquals(filter.getCategoryId()))
                    .and(MenuItemSpecification.statusEquals(filter.getStatus()));

            Page<MenuItemResponse> menuItemResponses = menuItemRepository.findAll(spec, pageable)
                    .map(menuItem -> new MenuItemResponse(
                            menuItem.getId(),
                            menuItem.getName(),
                            menuItem.getPrice(),
                            menuItem.getDescription(),
                            menuItem.getStatus().toString(),
                            menuItem.getCreatedAt(),
                            menuItem.getUpdatedAt(),
                            new CategoryMenuItemResponse(
                                    menuItem.getCategory().getId(),
                                    menuItem.getCategory().getName(),
                                    menuItem.getCategory().getCreatedAt(),
                                    menuItem.getCategory().getUpdatedAt()
                            ),
                            menuItem.getListImage().stream()
                                    .map(img -> new MenuItemResponse.ImageResponse(img.getName(), img.getUrl()))
                                    .collect(Collectors.toList())
                    ));

            PageResponse<MenuItemResponse> pageResponse = PageResponse.<MenuItemResponse>builder()
                    .totalItems(menuItemResponses.getTotalElements())
                    .totalPages(menuItemResponses.getTotalPages())
                    .currentPage(menuItemResponses.getNumber() + 1) // cộng 1 vì page index = 0
                    .objectJson(menuItemResponses.getContent())
                    .build();

            return wrapResponseFactory.success(pageResponse, "GET_ALL_MENU_ITEMS");

        } catch (Exception ex) {
            logger.error("Lỗi khi lấy danh sách món ăn có phân trang: ", ex);
            return wrapResponseFactory.error(
                    "GET_ALL_MENU_ITEMS",
                    "SYSTEM",
                    ex.getMessage(),
                    null
            );
        }
    }


    public MenuItemResponse getMenuItemById(UUID id) {
        MenuItem menuItem = menuItemRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy món ăn với ID: " + id));

        return new MenuItemResponse(
                menuItem.getId(),
                menuItem.getName(),
                menuItem.getPrice(),
                menuItem.getDescription(),
                menuItem.getStatus().toString(),
                menuItem.getCreatedAt(),
                menuItem.getUpdatedAt(),
                new CategoryMenuItemResponse(
                        menuItem.getCategory().getId(),
                        menuItem.getCategory().getName(),
                        menuItem.getCategory().getCreatedAt(),
                        menuItem.getCategory().getUpdatedAt()
                ),
                menuItem.getListImage().stream()
                        .map(img -> new MenuItemResponse.ImageResponse(img.getName(), img.getUrl()))
                        .collect(Collectors.toList())
        );
    }

    @Override
    public WrapResponse<PageResponse<MenuItemResponse>> getMenuItemsByCategory(UUID categoryId, Pageable pageable) {
        try {
            Page<MenuItemResponse> menuItemResponses = menuItemRepository
                    .findByCategoryIdAndIsDeletedFalse(categoryId, pageable)
                    .map(menuItemMapper::toDto);
            PageResponse<MenuItemResponse> pageResponse = PageResponse.<MenuItemResponse>builder()
                    .totalItems(menuItemResponses.getTotalElements())
                    .totalPages(menuItemResponses.getTotalPages())
                    .currentPage(pageable.getPageNumber() + 1)
                    .objectJson(menuItemResponses.getContent())
                    .build();

            return wrapResponseFactory.success(pageResponse, "GET_MENU_ITEMS_BY_CATEGORY");
        } catch (Exception ex) {
            return wrapResponseFactory.error(
                    "GET_MENU_ITEMS_BY_CATEGORY",
                    "SYSTEM",
                    ex.getMessage(),
                    null
            );
        }
    }

    @Override
    @Transactional
    public ResponseEntity<SuccessResponse> createMenuItem(MenuItemRequest request) throws IOException {
        logger.info("Bắt đầu tạo món ăn mới với tên: {}", request.getName());

        Location location = locationRepository.findById(request.getLocationId())
                .orElseThrow(() -> new RuntimeException("Không tìm thấy Locatio với ID: " + request.getLocationId()));

        Optional<MenuItem> menuItemFind = menuItemRepository.findByName(request.getName());

        if(menuItemFind.isPresent()){
            MenuItem existing = menuItemFind.get();
            if (existing.getLocation().getId().equals(location.getId())) {
                throw new RuntimeException("Món ăn này đã tồn tại trong nhà hàng này!");
            }
        }

        CategoryMenuItem category = categoryMenuItemRepository.findById(request.getCategoryId())
                .orElseThrow(() -> new RuntimeException("Không tìm thấy danh mục với ID: " + request.getCategoryId()));

        MenuItem menuItem = new MenuItem();
        menuItem.setName(request.getName());
        menuItem.setPrice(request.getPrice());
        menuItem.setDescription(request.getDescription());
        menuItem.setStatus(request.getStatus() != null ? request.getStatus() : MenuItemStatus.DANG_BAN);
        menuItem.setCategory(category);
        menuItem.setLocation(location);
        menuItem.setIsDeleted(false);

        // upload nhiều ảnh
        if (request.getFiles() != null && !request.getFiles().isEmpty()) {
            List<MenuItemImages> imageEntities = new ArrayList<>();
            for (MultipartFile file : request.getFiles()) {
                if (!file.isEmpty()) {
                    String imageUrl = cloudinaryService.getImageUrlAfterUpload(file, "menu-item");

                    MenuItemImages img = new MenuItemImages();
                    img.setName(file.getOriginalFilename());
                    img.setUrl(imageUrl);
                    img.setMenuItem(menuItem);


                    imageEntities.add(img);
                }
            }
            menuItem.setListImage(imageEntities);
        }

        MenuItem saved = menuItemRepository.save(menuItem);
        logger.info("Đã tạo món ăn thành công: {}", saved.getName());

        menuItemMapper.toDto(saved);
        SuccessResponse response = ResponseUtil.success("Tạo thành công", "/api/menu-items");
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }


    @Override
    @Transactional
    public ResponseEntity<SuccessResponse> updateMenuItem(MenuItemRequest request) throws IOException {
        UUID id = request.getId();
        logger.info("Bắt đầu cập nhật món ăn với ID: {}", id);

        MenuItem menuItem = menuItemRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy món ăn với ID: " + id));

        // kiểm tra trùng tên (trong cùng location, và nó)
        Optional<MenuItem> existingNameMenuItem = menuItemRepository.findByName(request.getName());
        if (existingNameMenuItem.isPresent()
                // nếu trùng id menuItem với nhau là true, ngược lại là false là bỏ qua if và cho update
                && !existingNameMenuItem.get().getId().equals(menuItem.getId()) // bỏ qua chính nó
                && existingNameMenuItem.get().getLocation().getId().equals(menuItem.getLocation().getId())) {
            throw new RuntimeException("Tên món ăn này đã tồn tại trong nhà hàng này!");
        }


        CategoryMenuItem category = categoryMenuItemRepository.findById(request.getCategoryId())
                .orElseThrow(() -> new RuntimeException("Không tìm thấy danh mục với ID: " + request.getCategoryId()));

        menuItem.setName(request.getName());
        menuItem.setPrice(request.getPrice());
        menuItem.setDescription(request.getDescription());
        menuItem.setStatus(request.getStatus() != null ? request.getStatus() : menuItem.getStatus());
        menuItem.setCategory(category);

        // Cập nhật ảnh nếu có upload mới
        if (request.getFiles() != null && !request.getFiles().isEmpty()) {
            // Xoá ảnh cũ trước
            if (menuItem.getListImage() != null && !menuItem.getListImage().isEmpty()) {
                menuItem.getListImage().clear();
            }

            List<MenuItemImages> newImages = new ArrayList<>();
            for (MultipartFile file : request.getFiles()) {
                if (!file.isEmpty()) {
                    String imageUrl = cloudinaryService.getImageUrlAfterUpload(file, "menu-item");

                    MenuItemImages img = new MenuItemImages();
                    img.setName(file.getOriginalFilename());
                    img.setUrl(imageUrl);
                    img.setMenuItem(menuItem);

                    newImages.add(img);
                }
            }
            menuItem.setListImage(newImages);
        }

        menuItemRepository.save(menuItem);
        logger.info("Cập nhật món ăn thành công: {}", menuItem.getName());

        SuccessResponse response = ResponseUtil.success(
                "Cập nhật món ăn thành công",
                "/api/menu-items/" + id
        );
        return ResponseEntity.ok(response);
    }


    @Override
    @Transactional
    public ResponseEntity<SuccessResponse> deleteMenuItem(UUID id) {
        MenuItem menuItem = menuItemRepository.findById(id)
                .orElseThrow(() -> {
                    logger.error("Không tìm thấy tài khoản có ID: {}", id);
                    return new RuntimeException("Không tìm thấy tài khoản có ID: " + id);
                });

        menuItemRepository.delete(menuItem);

        SuccessResponse response = ResponseUtil.success(
                "Xóa menuItem thành công",
                "/api/menu-items/" + id
        );

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @Override
    public WrapResponse<TotalMenuItemResponse> getTotalMenuItems() {
        Long total = menuItemRepository.countAllActiveMenuItems();

        TotalMenuItemResponse response = TotalMenuItemResponse.builder()
                .totalMenuItems(total)
                .build();

        return WrapResponse.<TotalMenuItemResponse>builder()
                .requestId(UUID.randomUUID().toString())
                .requestAction("GET_TOTAL_MENU_ITEMS")
                .error(false)
                .objectJson(response)
                .build();
    }

    @Override
    public WrapResponse<List<TopSellingMenuItemResponse>> getTopSellingMenuItems() {
        List<Object[]> results = orderDetailRepository.findTopSellingMenuItems();

        List<TopSellingMenuItemResponse> response = results.stream()
                .limit(10) // lấy top 10
                .map(r -> {
                    MenuItem menuItem = (MenuItem) r[0];
                    Long totalSold = (Long) r[1];
                    return new TopSellingMenuItemResponse(
                            menuItem.getId(),
                            menuItem.getName(),
                            menuItem.getPrice(),
                            totalSold
                    );
                })
                .toList();

        return WrapResponse.<List<TopSellingMenuItemResponse>>builder()
                .requestId(UUID.randomUUID().toString())
                .requestAction("GET_TOP_SELLING_MENU_ITEMS")
                .error(false)
                .objectJson(response)
                .build();
    }

    @Override
    public WrapResponse<PageResponse<MenuItemResponse>> getMenuItemsByLocation(UUID locationId, MenuItemFilterRequest filter, Pageable pageable) {
        logger.info("Bắt đầu lấy danh sách món ăn theo locationId: {}", locationId);
        try {
            Specification<MenuItem> spec = Specification
                    .where(MenuItemSpecification.locationEquals(locationId))
                    .and(MenuItemSpecification.nameContains(filter.getName()))
                    .and(MenuItemSpecification.categoryEquals(filter.getCategoryId()))
                    .and(MenuItemSpecification.statusEquals(filter.getStatus()));

            Page<MenuItemResponse> menuItemResponses = menuItemRepository.findAll(spec, pageable)
                    .map(menuItem -> new MenuItemResponse(
                            menuItem.getId(),
                            menuItem.getName(),
                            menuItem.getPrice(),
                            menuItem.getDescription(),
                            menuItem.getStatus().toString(),
                            menuItem.getCreatedAt(),
                            menuItem.getUpdatedAt(),
                            new CategoryMenuItemResponse(
                                    menuItem.getCategory().getId(),
                                    menuItem.getCategory().getName(),
                                    menuItem.getCategory().getCreatedAt(),
                                    menuItem.getCategory().getUpdatedAt()
                            ),
                            menuItem.getListImage().stream()
                                    .map(img -> new MenuItemResponse.ImageResponse(img.getName(), img.getUrl()))
                                    .collect(Collectors.toList())
                    ));

            PageResponse<MenuItemResponse> pageResponse = PageResponse.<MenuItemResponse>builder()
                    .totalItems(menuItemResponses.getTotalElements())
                    .totalPages(menuItemResponses.getTotalPages())
                    .currentPage(menuItemResponses.getNumber() + 1)
                    .objectJson(menuItemResponses.getContent())
                    .build();

            return wrapResponseFactory.success(pageResponse, "GET_MENU_ITEMS_BY_LOCATION");

        } catch (Exception ex) {
            logger.error("Lỗi khi lấy danh sách món ăn theo locationId: ", ex);
            return wrapResponseFactory.error(
                    "GET_MENU_ITEMS_BY_LOCATION",
                    "SYSTEM",
                    ex.getMessage(),
                    null
            );
        }
    }


}
