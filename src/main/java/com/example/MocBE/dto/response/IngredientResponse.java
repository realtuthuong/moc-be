package com.example.MocBE.dto.response;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class IngredientResponse {
    UUID id;
    String name;
    String description;
    boolean status;
    Integer minQuantity;
    Integer quantity;
    BigDecimal price;
    LocalDateTime createdAt;
    LocalDateTime updatedAt;
    String unitId;
    String unitName;
    String categoryIngredientId;
    String categoryIngredientName;
    String accountName;
}
