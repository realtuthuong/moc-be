package com.example.MocBE.dto.response;

import com.example.MocBE.enums.MembershipLevel;
import com.example.MocBE.model.Role;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CustomerResponse {
    UUID id;
    String fullName;
    String phone;
    String email;

    String address;
    LocalDateTime dateOfBirth;

    Integer totalOrders;
    BigDecimal totalSpent;
    MembershipLevel membershipLevel;

    LocalDateTime createdAt;
    LocalDateTime updatedAt;
}
