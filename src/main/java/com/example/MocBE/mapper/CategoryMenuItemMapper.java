package com.example.MocBE.mapper;

import com.example.MocBE.dto.response.CategoryMenuItemResponse;
import com.example.MocBE.model.CategoryMenuItem;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface CategoryMenuItemMapper {
    CategoryMenuItemResponse toDto(CategoryMenuItem categoryMenuItem);
    List<CategoryMenuItemResponse> toDtoList(List<CategoryMenuItem> categoryMenuItems);

}
