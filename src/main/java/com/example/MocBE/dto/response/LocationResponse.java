package com.example.MocBE.dto.response;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class LocationResponse {
    UUID id;
    String name;
    String address;
    Boolean status;
    LocalTime openTime;
    LocalTime closeTime;
    LocalDateTime createdAt;
    LocalDateTime updatedAt;

    String imageUrl;

    long totalTables;
    long totalOrders;
}
