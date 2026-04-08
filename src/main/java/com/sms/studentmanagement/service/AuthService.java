package com.sms.studentmanagement.service;

import com.sms.studentmanagement.dto.AuthDto;

public interface AuthService {
    AuthDto.JwtResponse login(AuthDto.LoginRequest loginRequest);
    AuthDto.JwtResponse register(AuthDto.RegisterRequest registerRequest);
    AuthDto.JwtResponse refreshToken(AuthDto.RefreshTokenRequest request);
}
