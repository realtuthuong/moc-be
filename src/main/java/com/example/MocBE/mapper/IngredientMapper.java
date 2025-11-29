package com.example.MocBE.mapper;

import com.example.MocBE.dto.response.IngredientResponse;
import com.example.MocBE.model.Ingredient;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface IngredientMapper {

    @Mapping(source = "unit.name", target = "unitName")
    @Mapping(source = "unit.id", target = "unitId")
    @Mapping(source = "account.fullName", target = "accountName")
    @Mapping(source = "categoryIngredient.name", target = "categoryIngredientName")
    @Mapping(source = "categoryIngredient.id", target = "categoryIngredientId")
    IngredientResponse toDto(Ingredient ingredient);

    List<IngredientResponse> toDtoList(List<Ingredient> ingredients);

}
