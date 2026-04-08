package com.sms.studentmanagement.controller;

import com.sms.studentmanagement.dto.ApiResponse;
import com.sms.studentmanagement.dto.AuthDto;
import com.sms.studentmanagement.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Tag(name = "Authentication", description = "Login, Register, and Token Refresh endpoints")
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    @Operation(summary = "Login user and get JWT tokens")
    public ResponseEntity<ApiResponse<AuthDto.JwtResponse>> login(
            @Valid @RequestBody AuthDto.LoginRequest loginRequest) {
        AuthDto.JwtResponse response = authService.login(loginRequest);
        return ResponseEntity.ok(ApiResponse.success("Login successful", response));
    }

    @PostMapping("/register")
    @Operation(summary = "Register a new user")
    public ResponseEntity<ApiResponse<AuthDto.JwtResponse>> register(
            @Valid @RequestBody AuthDto.RegisterRequest registerRequest) {
        AuthDto.JwtResponse response = authService.register(registerRequest);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("User registered successfully", response));
    }

    @PostMapping("/refresh-token")
    @Operation(summary = "Refresh access token using refresh token")
    public ResponseEntity<ApiResponse<AuthDto.JwtResponse>> refreshToken(
            @Valid @RequestBody AuthDto.RefreshTokenRequest request) {
        AuthDto.JwtResponse response = authService.refreshToken(request);
        return ResponseEntity.ok(ApiResponse.success("Token refreshed successfully", response));
    }
}
