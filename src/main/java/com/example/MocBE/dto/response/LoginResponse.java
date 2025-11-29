package com.example.MocBE.dto.response;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class LoginResponse {
    private AccountResponse account;   // dành cho nhân viên/admin
    private String customerEmail;      // dành cho customer
    private AuthInfo authInfo;
    private String exp;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    @FieldDefaults(level = AccessLevel.PRIVATE)
    public static class AuthInfo {
        String accessToken;
        String tokenType;
        String message;
    }
}



