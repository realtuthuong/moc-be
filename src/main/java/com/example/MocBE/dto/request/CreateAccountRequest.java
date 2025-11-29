package com.example.MocBE.dto.request;

import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CreateAccountRequest {
    String username;
    String password;
    String email;
    String phone;
    UUID roleId;
    UUID locationId;
    String fullName;
    MultipartFile file;
    Long salary;
}
