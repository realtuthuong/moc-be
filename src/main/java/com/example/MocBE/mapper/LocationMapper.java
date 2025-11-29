package com.example.MocBE.mapper;

import com.example.MocBE.dto.request.LocationRequest;
import com.example.MocBE.dto.response.LocationResponse;
import com.example.MocBE.model.Location;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface LocationMapper {

    @Mapping(target = "totalTables", expression = "java(location.getListTable() != null ? location.getListTable().size() : 0)")
    @Mapping(target = "totalOrders", expression = "java(location.getListOrder() != null ? location.getListOrder().size() : 0)")
    LocationResponse toDto(Location location);
    List<LocationResponse> toDtoList(List<Location> locations);

    // Mapping cho create
    Location toEntity(LocationRequest request);
}
