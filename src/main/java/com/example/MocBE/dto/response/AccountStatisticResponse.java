package com.example.MocBE.dto.response;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.util.UUID;

@Data
@Builder
public class AccountStatisticResponse {
    UUID id;
    String fullName;
    Long salary;
    Long totalOrders;
    BigDecimal totalRevenue;
}
