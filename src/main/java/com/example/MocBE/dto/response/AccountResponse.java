package com.example.MocBE.dto.response;

import com.example.MocBE.model.Role;
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
public class AccountResponse {
    UUID id;
    String username;
    String avatarUrl;
    String fullName;
    String phone;
    String email;
    LocalDateTime createdAt;
    LocalDateTime updatedAt;
    Role role;
    Long salary;
    String locationName;
    UUID locationId;
    Boolean isDeleted;
}
