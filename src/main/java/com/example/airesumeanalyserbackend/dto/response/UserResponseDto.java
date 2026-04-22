package com.example.airesumeanalyserbackend.dto.response;

import java.util.List;

public record UserResponseDto(String userId, String username, String email, List<ResumeResponseDto> resumes) {
}
