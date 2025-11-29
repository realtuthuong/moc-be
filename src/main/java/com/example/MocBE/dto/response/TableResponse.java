package com.example.MocBE.dto.response;

import com.example.MocBE.enums.TableStatus;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class TableResponse {
     UUID id;
     String name;
     TableStatus status;
     byte capacity;
     String createdAt;
     String updatedAt;
     UUID locationId;
     String locationName;

}
