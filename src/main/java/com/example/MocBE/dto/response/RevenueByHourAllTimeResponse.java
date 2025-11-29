package com.example.MocBE.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import java.math.BigDecimal;

@Data
@AllArgsConstructor
public class RevenueByHourAllTimeResponse {
    private Integer hour;       // giờ trong ngày (0-23)
    private BigDecimal revenue; // tổng doanh thu của tất cả order vào giờ này
}