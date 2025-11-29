package com.example.MocBE.dto.request;

import lombok.Data;

@Data
public class AccountFilterRequest {
    private String username;
    private String fullName;
    private String roleName;
    private String phone;
    private String email;
}