package com.example.MocBE.controller;

import com.example.MocBE.dto.request.AuthRequest;
import com.example.MocBE.dto.request.RegisterRequest;
import com.example.MocBE.dto.response.AuthResponse;
import com.example.MocBE.dto.response.LoginResponse;
import com.example.MocBE.dto.response.SuccessResponse;
import com.example.MocBE.dto.response.WrapResponse;
import com.example.MocBE.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    public WrapResponse<LoginResponse> login(@RequestBody AuthRequest request) {
        return authService.authenticate(request);
    }

    @PostMapping("/register")
    public ResponseEntity<SuccessResponse> register(@RequestBody RegisterRequest request) {
        return authService.register(request);
    }

    @GetMapping("/verify-email")
    public ResponseEntity<?> verifyEmail(@RequestParam String token) {
        return ResponseEntity.ok(authService.verifyEmail(token));
    }
}
