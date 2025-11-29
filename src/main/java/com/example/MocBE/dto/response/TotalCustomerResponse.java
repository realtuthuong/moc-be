package com.example.MocBE.dto.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TotalCustomerResponse {
    private Long totalCustomers;
}
