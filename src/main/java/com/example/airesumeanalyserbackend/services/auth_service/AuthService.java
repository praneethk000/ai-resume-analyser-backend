package com.example.airesumeanalyserbackend.services.auth_service;

import com.example.airesumeanalyserbackend.dto.request.LoginRequestDto;
import com.example.airesumeanalyserbackend.dto.request.RegisterRequestDto;
import com.example.airesumeanalyserbackend.dto.response.AuthResponseDto;

public interface AuthService {
    public AuthResponseDto register(RegisterRequestDto registerRequestDto);

    public AuthResponseDto login(LoginRequestDto loginRequestDto);
}
