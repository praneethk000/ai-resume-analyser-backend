package com.example.airesumeanalyserbackend.dto.response;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public record ResumeResponseDto(String resumeId, String title, String firstName, String lastName, String phoneNumber,
        String email, String linkedin, String github, String address, String city, String state, String pincode,
        String nationality, String gender, LocalDate dob, String resumeUrl, String resumeFileName,
        LocalDateTime resumeUploadedAt, List<String> skills) {
}
