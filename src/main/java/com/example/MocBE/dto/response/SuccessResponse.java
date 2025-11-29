package com.example.MocBE.dto.response;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class SuccessResponse {
    LocalDateTime timestamp;
    int status;
    String message;
    String path;
}
