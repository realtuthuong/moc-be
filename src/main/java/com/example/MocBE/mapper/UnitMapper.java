package com.example.MocBE.mapper;

import com.example.MocBE.dto.UnitDto;
import com.example.MocBE.dto.response.RoleResponse;
import com.example.MocBE.model.Role;
import com.example.MocBE.model.Unit;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UnitMapper {
    UnitDto toDto(Unit unit);

}
