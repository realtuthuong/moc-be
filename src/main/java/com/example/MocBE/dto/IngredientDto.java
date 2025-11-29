package com.example.MocBE.dto;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;
import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class IngredientDto {
    UUID id;
    String name;
    String description;
    Integer minQuantity;
    Integer quantity;
    BigDecimal price;
    Boolean status;
    UUID unitId;
    UUID categoryIngredientId;
    UUID accountId;
    String accountName;
    String categoryIngredientName;
    String unitName;
}
