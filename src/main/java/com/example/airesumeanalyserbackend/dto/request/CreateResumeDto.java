package com.example.airesumeanalyserbackend.dto.request;

import com.example.airesumeanalyserbackend.models.Gender;

import java.time.LocalDate;

public record CreateResumeDto(String userId, String title, String firstName, String lastName, String phoneNumber, String email, String linkedin, String github, String address, String city, String state, String pincode, String nationality, Gender gender, LocalDate dob, String resumeUrl, String resumeFileName, String resumeParsedText) {
}
