package com.example.MocBE.dto.request;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;
import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class IngredientRequest {
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
    UUID locationId;
}
