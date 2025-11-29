package com.example.MocBE.dto.response;

import com.example.MocBE.enums.OrderStatus;
import com.example.MocBE.enums.OrderType;
import com.example.MocBE.model.*;
import com.example.MocBE.model.Table;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class OrderResponse {
    UUID id;
    OrderStatus statusOrder;
    OrderType orderType;
    LocalDateTime createdAt;
    BigDecimal totalPrice;
    String note;
    LocalDateTime reservationDate;
    int guestCount;
    List<OrderDetailResponse> details;

    UUID tableId;
    String tableName;

    UUID locationId;
    String locationName;

    UUID customerId;
    String customerName;
    String customerPhone;

    UUID accountId;
    String accountName;

    UUID invoiceId;
    BigDecimal invoiceTotal;
    String invoiceStatus;
}
