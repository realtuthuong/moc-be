package com.example.MocBE.mapper;

import com.example.MocBE.dto.response.CategoryIngredientResponse;
import com.example.MocBE.dto.response.CategoryMenuItemResponse;
import com.example.MocBE.model.CategoryIngredient;
import com.example.MocBE.model.CategoryMenuItem;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface CategoryIngredientMapper {
    CategoryIngredientResponse toDto(CategoryIngredient categoryIngredient);
    List<CategoryIngredientResponse> toDtoList(List<CategoryIngredient> categoryIngredients);
}
