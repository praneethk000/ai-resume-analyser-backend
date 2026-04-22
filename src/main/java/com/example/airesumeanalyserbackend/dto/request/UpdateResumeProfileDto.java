package com.example.airesumeanalyserbackend.dto.request;

import java.time.LocalDate;

import com.example.airesumeanalyserbackend.models.Gender;

public record UpdateResumeProfileDto(String resumeId, String firstName, String lastName, String phoneNumber,
        String email, String linkedin, String github, String address, String city, String state, String pincode,
        String nationality, LocalDate dob, Gender gender) {
}
