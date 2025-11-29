package com.example.MocBE.mapper;

import com.example.MocBE.dto.response.TableResponse;
import com.example.MocBE.model.Table;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface TableMapper {

    @Mapping(source = "location.name", target = "locationName")
    TableResponse toDto(Table table);
    List<TableResponse> toDtoList(List<Table> tables);
}
