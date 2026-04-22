package com.example.airesumeanalyserbackend.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.airesumeanalyserbackend.dto.request.LoginRequestDto;
import com.example.airesumeanalyserbackend.dto.request.RegisterRequestDto;
import com.example.airesumeanalyserbackend.dto.response.AuthResponseDto;
import com.example.airesumeanalyserbackend.services.auth_service.AuthService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/web/api/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/v1/registerUser")
    public ResponseEntity<AuthResponseDto> registerUser(@Valid @RequestBody RegisterRequestDto registerRequestDto) {
        return ResponseEntity.ok(authService.register(registerRequestDto));
    }

    @PostMapping("/v1/loginUser")
    public ResponseEntity<AuthResponseDto> loginUser(@Valid @RequestBody LoginRequestDto loginRequestDto) {
        return ResponseEntity.ok(authService.login(loginRequestDto));
    }
}
