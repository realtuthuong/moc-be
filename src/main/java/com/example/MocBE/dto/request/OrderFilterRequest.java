package com.example.MocBE.dto.request;
import com.example.MocBE.enums.OrderStatus;
import com.example.MocBE.enums.OrderType;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class OrderFilterRequest {
    private OrderStatus statusOrder;
    private OrderType orderType;
    private String customerName;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
}