package com.example.MocBE.service.imp;

import com.example.MocBE.dto.IngredientDto;
import com.example.MocBE.dto.request.IngredientFilterRequest;
import com.example.MocBE.dto.request.IngredientRequest;
import com.example.MocBE.dto.request.UpdateAccountRequest;
import com.example.MocBE.dto.response.*;
import com.example.MocBE.exception.GlobalExceptionHandler;
import com.example.MocBE.exception.RegistrationException;
import com.example.MocBE.mapper.IngredientMapper;
import com.example.MocBE.mapper.WrapResponseFactory;
import com.example.MocBE.model.*;
import com.example.MocBE.repository.*;
import com.example.MocBE.repository.spec.IngredientSpecification;
import com.example.MocBE.service.IngredientService;
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

import java.io.IOException;
import java.util.UUID;


@Service
@RequiredArgsConstructor
public class IngredientServiceImpl implements IngredientService {
    private static final Logger logger = LoggerFactory.getLogger(IngredientServiceImpl.class);

    private final IngredientRepository ingredientRepository;
    private final AccountRepository accountRepository;
    private final UnitRepository unitRepository;
    private final CategoryIngredientRepository categoryIngredientRepository;
    private final IngredientMapper ingredientMapper;
    private final WrapResponseFactory wrapResponseFactory;
    private final LocationRepository locationRepository;

    @Override
    public WrapResponse<PageResponse<IngredientResponse>> getAllIngredients(IngredientFilterRequest filterRequest, Pageable pageable) {
        try {
            Specification<Ingredient> spec = Specification
                    .where(IngredientSpecification.nameContains(filterRequest.getName()))
                    .and(IngredientSpecification.categoryIngredientEquals(filterRequest.getCategoryIngredientName()))
                    .and(IngredientSpecification.unitEquals(filterRequest.getUnitName()))
                    .and(IngredientSpecification.statusEquals(filterRequest.getStatus()))
                    .and(IngredientSpecification.isNotDeleted());


            Page<IngredientResponse> ingredients = ingredientRepository.findAll(spec, pageable)
                    .map(ingredientMapper::toDto);

            PageResponse<IngredientResponse> pageResponse = PageResponse.<IngredientResponse>builder()
                    .totalItems(ingredients.getTotalElements())
                    .totalPages(ingredients.getTotalPages())
                    .currentPage(pageable.getPageNumber() + 1)
                    .objectJson(ingredients.getContent())
                    .build();

            return wrapResponseFactory.success(pageResponse, "GET_ALL_INGREDIENTS");

        } catch (Exception ex) {
            logger.error("Lỗi khi lấy danh sách ingredients: ", ex);
            return wrapResponseFactory.error(
                    "GET_ALL_INGREDIENTS",
                    "SYSTEM",
                    ex.getMessage(),
                    null
            );
        }
    }

    @Override
    public WrapResponse<PageResponse<IngredientResponse>> getIngredientsByLocation(UUID locationId, IngredientFilterRequest filterRequest, Pageable pageable) {
        try {
            Specification<Ingredient> spec = Specification
                    .where(IngredientSpecification.locationEquals(locationId)) // thêm filter theo location
                    .and(IngredientSpecification.nameContains(filterRequest.getName()))
                    .and(IngredientSpecification.categoryIngredientEquals(filterRequest.getCategoryIngredientName()))
                    .and(IngredientSpecification.unitEquals(filterRequest.getUnitName()))
                    .and(IngredientSpecification.statusEquals(filterRequest.getStatus()))
                    .and(IngredientSpecification.isNotDeleted());

            Page<IngredientResponse> ingredients = ingredientRepository.findAll(spec, pageable)
                    .map(ingredientMapper::toDto);

            PageResponse<IngredientResponse> pageResponse = PageResponse.<IngredientResponse>builder()
                    .totalItems(ingredients.getTotalElements())
                    .totalPages(ingredients.getTotalPages())
                    .currentPage(pageable.getPageNumber() + 1)
                    .objectJson(ingredients.getContent())
                    .build();

            return wrapResponseFactory.success(pageResponse, "GET_INGREDIENTS_BY_LOCATION");

        } catch (Exception ex) {
            logger.error("Lỗi khi lấy danh sách ingredients theo location: ", ex);
            return wrapResponseFactory.error(
                    "GET_INGREDIENTS_BY_LOCATION",
                    "SYSTEM",
                    ex.getMessage(),
                    null
            );
        }
    }


    @Override
    @Transactional
    public ResponseEntity<SuccessResponse> createIngredient(IngredientRequest request) {

        Unit unit = unitRepository.findById(request.getUnitId())
                .orElseThrow(() -> new GlobalExceptionHandler.NotFoundException(
                        "Không tìm thấy name unit = " + request.getUnitId()));

        Location location = locationRepository.findById(request.getLocationId())
                .orElseThrow(() -> new GlobalExceptionHandler.NotFoundException(
                        "Không tìm thấy name locationID = " + request.getLocationId()));

        CategoryIngredient categoryIngredient = categoryIngredientRepository.findById(request.getCategoryIngredientId())
                .orElseThrow(() -> new GlobalExceptionHandler.NotFoundException(
                        "Không tìm thấy categoryIngredientId = " + request.getCategoryIngredientId()));

        Account account = accountRepository.findById(request.getAccountId())
                .orElseThrow(() -> new GlobalExceptionHandler.NotFoundException(
                        "Không tìm thấy Account với accountId = " + request.getAccountId()));

        Ingredient newIngredient = new Ingredient();
        newIngredient.setName(request.getName());
        newIngredient.setPrice(request.getPrice());
        newIngredient.setDescription(request.getDescription());
        newIngredient.setIsDeleted(false);
        newIngredient.setStatus(false);
        newIngredient.setCategoryIngredient(categoryIngredient);
        newIngredient.setMinQuantity(request.getMinQuantity());
        newIngredient.setQuantity(request.getQuantity());
        newIngredient.setAccount(account);
        newIngredient.setUnit(unit);
        newIngredient.setLocation(location);

        Ingredient ingredient = ingredientRepository.save(newIngredient);
        logger.info("Đã tạo Ingredient mới: {}", ingredient.getName());

        SuccessResponse response = ResponseUtil.success("Tạo Ingredient thành công", "/api/ingredient");
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }



    @Override
    @Transactional
    public ResponseEntity<SuccessResponse> deleteIngredient(UUID id) {
        Ingredient ingredient = ingredientRepository.findById(id)
                .orElseThrow(() -> {
                    logger.error("Không tìm thấy ingredient có ID: {}", id);
                    return new RuntimeException("Không tìm thấy ingredient có ID: " + id);
                });
        ingredient.setIsDeleted(true);
        ingredientRepository.save(ingredient);
        SuccessResponse response = ResponseUtil.success(
                "Xóa ingredient thành công",
                "/api/ingredient/" + id
        );
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @Override
    @Transactional
    public ResponseEntity<SuccessResponse> updateIngredient(IngredientDto request) throws IOException {
        if (request.getId() == null) {
            throw new GlobalExceptionHandler.BadRequestException("Id của ingredient không được để trống");
        }

        Ingredient ingredient = ingredientRepository.findById(request.getId())
                .orElseThrow(() -> new GlobalExceptionHandler.NotFoundException(
                        "Không tìm thấy Ingredient với id = " + request.getId()
                ));

        ingredient.setName(request.getName());
        ingredient.setPrice(request.getPrice());
        ingredient.setDescription(request.getDescription());
        ingredient.setStatus(request.getStatus() != null ? request.getStatus() : ingredient.isStatus());
        ingredient.setMinQuantity(request.getMinQuantity());
        ingredient.setQuantity(request.getQuantity());

        if (request.getUnitId() != null) {
            Unit unit = unitRepository.findById(request.getUnitId())
                    .orElseThrow(() -> new GlobalExceptionHandler.NotFoundException(
                            "Không tìm thấy Unit với id = " + request.getUnitId()
                    ));
            ingredient.setUnit(unit);
        }

        if (request.getCategoryIngredientId() != null) {
            CategoryIngredient categoryIngredient = categoryIngredientRepository.findById(request.getCategoryIngredientId())
                    .orElseThrow(() -> new GlobalExceptionHandler.NotFoundException(
                            "Không tìm thấy CategoryIngredient với id = " + request.getCategoryIngredientId()
                    ));
            ingredient.setCategoryIngredient(categoryIngredient);
        }

        if (request.getAccountId() != null) {
            Account account = accountRepository.findById(request.getAccountId())
                    .orElseThrow(() -> new GlobalExceptionHandler.NotFoundException(
                            "Không tìm thấy Account với id = " + request.getAccountId()
                    ));
            ingredient.setAccount(account);
        }

        ingredientRepository.save(ingredient);
        logger.info("Đã cập nhật Ingredient: {}", ingredient.getName());

        SuccessResponse response = ResponseUtil.success(
                "Cập nhật ingredient thành công",
                "/api/ingredient/" + request.getId()
        );
        return ResponseEntity.ok(response);
    }

}
