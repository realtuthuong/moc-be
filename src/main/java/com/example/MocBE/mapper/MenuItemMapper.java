package com.example.MocBE.mapper;

import com.example.MocBE.dto.response.MenuItemResponse;
import com.example.MocBE.model.MenuItem;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring", uses = {MenuItemImagesMapper.class})
public interface MenuItemMapper {

    @Mapping(source = "listImage", target = "images")
    MenuItemResponse toDto(MenuItem menuItem);

    List<MenuItemResponse> toDtoList(List<MenuItem> menuItems);
}
