package com.example.MocBE.dto.request;

import com.example.MocBE.enums.MenuItemStatus;
import jakarta.validation.constraints.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Data
public class MenuItemRequest {
    private UUID id;
    @NotBlank
    private String name;
    @NotNull
    private BigDecimal price;
    private String description;
    private MenuItemStatus status;
    @NotNull
    private UUID categoryId;
    private UUID locationId;
    private List<MultipartFile> files;

}
