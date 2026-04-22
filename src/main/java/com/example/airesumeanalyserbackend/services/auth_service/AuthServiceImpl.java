package com.example.airesumeanalyserbackend.services.auth_service;

import java.time.LocalDateTime;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.airesumeanalyserbackend.dto.request.LoginRequestDto;
import com.example.airesumeanalyserbackend.dto.request.RegisterRequestDto;
import com.example.airesumeanalyserbackend.dto.response.AuthResponseDto;
import com.example.airesumeanalyserbackend.exceptions.ApiRequestException;
import com.example.airesumeanalyserbackend.models.User;
import com.example.airesumeanalyserbackend.repositories.UserRepository;
import com.example.airesumeanalyserbackend.security.JwtService;
import com.example.airesumeanalyserbackend.utils.UUIDService;

@Service
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final UUIDService uuidService;

    public AuthServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder, JwtService jwtService,
            AuthenticationManager authenticationManager, UUIDService uuidService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
        this.authenticationManager = authenticationManager;
        this.uuidService = uuidService;
    }

    @Override
    public AuthResponseDto register(RegisterRequestDto registerRequestDto) {
        if (userRepository.findById(registerRequestDto.email()).isPresent()) {
            throw new ApiRequestException("Email already exists.");
        }

        User user = new User();
        user.setUserId(uuidService.generateUUID());
        user.setUsername(registerRequestDto.username());
        user.setEmail(registerRequestDto.email());
        user.setPassword(registerRequestDto.password());
        user.setCreatedAt(LocalDateTime.now());

        User savedUser = userRepository.save(user);

        String jwtToken = jwtService.generateToken(savedUser.getUsername());

        return new AuthResponseDto(jwtToken, savedUser.getUserId(), savedUser.getUsername());
    }

    @Override
    public AuthResponseDto login(LoginRequestDto loginRequestDto) {

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequestDto.email(), loginRequestDto.password())

        );

        User user = userRepository.findByEmail(loginRequestDto.email())
                .orElseThrow(() -> new ApiRequestException("User not found."));

        String jwtToken = jwtService.generateToken(user.getUsername());

        return new AuthResponseDto(jwtToken, user.getUserId(), user.getUsername());

    }

}
