package com.example.MocBE.dto.request;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CustomerOrderReservationRequest {
    String fullName;
    String phone;
    String email;
    List<ItemRequest> listItem;
    LocalDateTime reservationDate;
    UUID locationId;
    UUID tableId;
    int guestCount;
    String note;

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @FieldDefaults(level = AccessLevel.PRIVATE)
    public static class ItemRequest {
        UUID menuItemId;
        int quantity;
        String note;
    }
}
