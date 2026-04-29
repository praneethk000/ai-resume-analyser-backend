package com.example.airesumeanalyserbackend.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.airesumeanalyserbackend.dto.request.LoginRequestDto;
import com.example.airesumeanalyserbackend.dto.request.RegisterRequestDto;
import com.example.airesumeanalyserbackend.dto.response.AuthResponseDto;
import com.example.airesumeanalyserbackend.models.User;
import com.example.airesumeanalyserbackend.repositories.UserRepository;
import com.example.airesumeanalyserbackend.exceptions.ApiRequestException;
import com.example.airesumeanalyserbackend.services.auth_service.AuthService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/web/api/auth")
public class AuthController {

    private final AuthService authService;
    private final UserRepository userRepository;

    public AuthController(AuthService authService, UserRepository userRepository) {
        this.authService = authService;
        this.userRepository = userRepository;
    }

    @PostMapping("/v1/registerUser")
    public ResponseEntity<AuthResponseDto> registerUser(@Valid @RequestBody RegisterRequestDto registerRequestDto) {
        return ResponseEntity.ok(authService.register(registerRequestDto));
    }

    @PostMapping("/v1/loginUser")
    public ResponseEntity<AuthResponseDto> loginUser(@Valid @RequestBody LoginRequestDto loginRequestDto) {
        return ResponseEntity.ok(authService.login(loginRequestDto));
    }

    /**
     * Returns the full user info for the currently authenticated user.
     * Used by the OAuth2 callback page to exchange its token for userId + username.
     * The token is already validated by JwtAuthenticationFilter before this runs.
     */
    @GetMapping("/v1/me")
    public ResponseEntity<AuthResponseDto> getCurrentUser(@AuthenticationPrincipal User currentUser) {
        if (currentUser == null) {
            throw new ApiRequestException("User not authenticated");
        }
        // Re-use the same response shape as login/register (token field will be null here — frontend already has it)
        return ResponseEntity.ok(new AuthResponseDto(
                null,
                currentUser.getUserId(),
                currentUser.getDisplayName(),
                currentUser.getEmail()
        ));
    }
}
