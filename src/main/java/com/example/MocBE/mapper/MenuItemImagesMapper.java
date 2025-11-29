package com.example.MocBE.mapper;

import com.example.MocBE.dto.response.MenuItemResponse;
import com.example.MocBE.model.MenuItemImages;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface MenuItemImagesMapper {
    MenuItemResponse.ImageResponse toDto(MenuItemImages menuItemImage);
    List<MenuItemResponse.ImageResponse> toDtoList(List<MenuItemImages> menuItemImages);
}
