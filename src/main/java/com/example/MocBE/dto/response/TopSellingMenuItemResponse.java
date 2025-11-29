package com.example.MocBE.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import java.math.BigDecimal;
import java.util.UUID;

@Data
@AllArgsConstructor
public class TopSellingMenuItemResponse {
    private UUID id;
    private String name;
    private BigDecimal price;
    private Long totalSold;
}