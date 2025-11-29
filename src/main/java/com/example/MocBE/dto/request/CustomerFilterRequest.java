package com.example.MocBE.dto.request;

import com.example.MocBE.enums.MembershipLevel;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class CustomerFilterRequest {
    // Search theo tên/email/sđt (dùng OR)
    private String keyword;

    // Lọc theo ngày tạo (khoảng thời gian)
    private LocalDate createdFrom;
    private LocalDate createdTo;

    private Integer birthMonth;
    private String city;
    private MembershipLevel membershipLevel;

    // Lọc theo khoảng tổng đơn hàng
    private Integer minTotalOrders;
    private Integer maxTotalOrders;

    // Lọc theo khoảng tổng chi tiêu
    private BigDecimal minTotalSpent;
    private BigDecimal maxTotalSpent;
}
