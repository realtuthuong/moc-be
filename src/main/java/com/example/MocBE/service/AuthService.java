package com.example.MocBE.service;


import com.example.MocBE.dto.request.AuthRequest;
import com.example.MocBE.dto.request.RegisterRequest;
import com.example.MocBE.dto.response.*;
import org.springframework.http.ResponseEntity;

public interface AuthService {

    WrapResponse<LoginResponse> authenticate(AuthRequest request);
    ResponseEntity<SuccessResponse> register(RegisterRequest request);
    String verifyEmail(String token);

}

