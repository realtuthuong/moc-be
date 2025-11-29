package com.example.MocBE.dto.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TotalOrderStatusTodayResponse {
    private Long totalDangChuanBiToday;
    private Long totalDaHoanThanhToday;
}