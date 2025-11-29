package com.example.MocBE.dto.request;

import jakarta.persistence.Column;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CustomerRequest {
    UUID id;
    @NotBlank(message = "Full name is required")
    String fullName;
    String phone;
    @Email(message = "Email không hợp lệ")
    String email;
    LocalDateTime dateOfBirth;
    String address;
}
