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
public class StaffOrderRequest {
    int guestCount;
    UUID accountId;
    String note;
    UUID tableId;
    LocalDateTime reservationDate;
    List<CustomerOrderReservationRequest.ItemRequest> listItem;
}
