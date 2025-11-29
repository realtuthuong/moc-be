package com.example.MocBE.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.util.UUID;

@Data
public class CategoryIngredientRequest {
    private UUID id;
    @NotBlank(message = "name not null")
    private String name;
    private UUID locationId;

}
