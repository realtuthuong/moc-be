package com.example.MocBE.dto.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TotalOrderResponse {
    private Long totalOrdersMonth;
    private Long totalOrdersYear;
}
