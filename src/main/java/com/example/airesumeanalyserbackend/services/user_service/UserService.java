package com.example.airesumeanalyserbackend.services.user_service;

import com.example.airesumeanalyserbackend.dto.request.CreateUserDto;
import com.example.airesumeanalyserbackend.dto.response.UserResponseDto;

import java.util.List;

public interface UserService {
    String createUser(CreateUserDto createUserDto);
    UserResponseDto getUserById(String id);
    List<UserResponseDto> getAllUsers();
}
