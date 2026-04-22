package com.example.airesumeanalyserbackend.dto.response;

import java.time.LocalDateTime;

public record ResumeAnalysisResponseDto(String resumeAnalysisId, String resumeId, String jobId, Float matchScore,String matchedSkills, String missingSkills, String suggestions, LocalDateTime createdAt) {
}
