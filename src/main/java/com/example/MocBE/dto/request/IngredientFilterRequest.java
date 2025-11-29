package com.example.MocBE.dto.request;

import lombok.Data;

@Data
public class IngredientFilterRequest {
    private String name;
    private Boolean status;
    private String categoryIngredientName;
    private String unitName;
}
