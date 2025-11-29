package com.example.MocBE.dto.response;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class MenuItemResponse {
    UUID id;
    String name;
    BigDecimal price;
    String description;
    String status;
    LocalDateTime createdAt;
    LocalDateTime updatedAt;
    CategoryMenuItemResponse category;
    List<ImageResponse> images;

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @FieldDefaults(level = AccessLevel.PRIVATE)
    public static class ImageResponse {
        String name;
        String url;
    }
}
