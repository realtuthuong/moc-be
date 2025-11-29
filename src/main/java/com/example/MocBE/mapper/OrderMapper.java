package com.example.MocBE.mapper;

import com.example.MocBE.dto.response.OrderDetailResponse;
import com.example.MocBE.dto.response.OrderResponse;
import com.example.MocBE.model.Order;
import com.example.MocBE.model.OrderDetail;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

import java.util.List;

@Mapper(componentModel = "spring")
public interface OrderMapper {

    @Mapping(target = "details", expression = "java(mapDetails(order.getListOrderDetail()))")
    @Mapping(target = "tableId", source = "table.id")
    @Mapping(target = "tableName", source = "table.name")
    @Mapping(target = "locationId", source = "location.id")
    @Mapping(target = "locationName", source = "location.name")
    @Mapping(target = "customerId", source = "customer.id")
    @Mapping(target = "customerName", source = "customer.fullName")
    @Mapping(target = "customerPhone", source = "customer.phone")
    @Mapping(target = "accountId", source = "account.id")
    @Mapping(target = "accountName", source = "account.fullName")
    @Mapping(target = "invoiceId", source = "invoice.id")
    @Mapping(target = "invoiceTotal", source = "invoice.totalAmount")
    @Mapping(target = "invoiceStatus", source = "invoice.status")
    OrderResponse todo(Order order);

    List<OrderResponse> todoList(List<Order> orders);

    // custom helper
    default List<OrderDetailResponse> mapDetails(List<OrderDetail> list) {
        if (list == null) return null;
        return list.stream().map(d ->
                OrderDetailResponse.builder()
                        .id(d.getId())
                        .menuItemName(d.getMenuItem().getName())
                        .quantity(d.getQuantity())
                        .note(d.getNote())
                        .priceAtOrder(d.getPriceAtOrder())
                        .build()
        ).toList();
    }
}
