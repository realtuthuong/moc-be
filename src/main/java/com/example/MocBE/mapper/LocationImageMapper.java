package com.example.MocBE.mapper;


import com.example.MocBE.dto.response.MenuItemResponse;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface LocationImageMapper {
    MenuItemResponse.ImageResponse toDto(MenuItemResponse.ImageResponse image);
}
