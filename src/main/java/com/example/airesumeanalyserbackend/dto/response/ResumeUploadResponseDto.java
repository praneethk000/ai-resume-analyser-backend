package com.example.airesumeanalyserbackend.dto.response;

import java.util.List;

public record ResumeUploadResponseDto(String resumeId, List<String> extractedSkills) {

}
