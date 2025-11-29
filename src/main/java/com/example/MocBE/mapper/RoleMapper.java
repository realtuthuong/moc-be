package com.example.MocBE.mapper;

import com.example.MocBE.dto.response.RoleResponse;
import com.example.MocBE.model.Role;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface RoleMapper {
    RoleResponse toDto(Role role);
}
