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
public class UpdateAccountRequest {
    UUID id;
    String username;
    String fullName;
    String phone;
    String email;
    String password;
    UUID locationId;
    UUID RoleId;
    MultipartFile file;
    Long salary;

}
