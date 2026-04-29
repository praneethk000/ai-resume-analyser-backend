package com.example.airesumeanalyserbackend.controllers;

import com.example.airesumeanalyserbackend.dto.request.CreateJobDescriptionDto;
import com.example.airesumeanalyserbackend.dto.response.JobDescriptionResponseDto;
import com.example.airesumeanalyserbackend.exceptions.ApiRequestException;
import com.example.airesumeanalyserbackend.models.User;
import com.example.airesumeanalyserbackend.services.job_description_service.JobDescriptionService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/web/api/jobDescription")
public class JobDescriptionController {

    private final JobDescriptionService jobDescriptionService;

    public JobDescriptionController(JobDescriptionService jobDescriptionService) {
        this.jobDescriptionService = jobDescriptionService;
    }

    @PostMapping("/v1/createJobDescription")
    public ResponseEntity<JobDescriptionResponseDto> createJobDescription(
            @RequestBody CreateJobDescriptionDto createJobDescriptionDto,
            @AuthenticationPrincipal User currentUser) {
        if (currentUser == null) {
            throw new ApiRequestException("Authentication required");
        }
        JobDescriptionResponseDto created = jobDescriptionService.createJobDescription(createJobDescriptionDto);
        return ResponseEntity.ok(created);
    }

    @GetMapping("/v1/displayJobById")
    public ResponseEntity<JobDescriptionResponseDto> getJobById(
            @RequestParam String jobId,
            @AuthenticationPrincipal User currentUser) {
        if (currentUser == null) {
            throw new ApiRequestException("Authentication required");
        }
        return ResponseEntity.ok(jobDescriptionService.getJobById(jobId));
    }

    // displayAllJobs removed — no user in this system should be able to
    // list every job description ever created by every user.
}
