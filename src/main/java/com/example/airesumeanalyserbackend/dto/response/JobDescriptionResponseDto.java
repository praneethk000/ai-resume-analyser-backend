package com.example.airesumeanalyserbackend.dto.response;

import java.time.LocalDateTime;

public record JobDescriptionResponseDto(String jobId, String jobTitle, String companyName, String jobDescriptionText, LocalDateTime createdAt) {
}
