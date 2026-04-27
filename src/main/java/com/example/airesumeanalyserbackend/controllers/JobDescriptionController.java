package com.example.airesumeanalyserbackend.controllers;

import com.example.airesumeanalyserbackend.dto.request.CreateJobDescriptionDto;
import com.example.airesumeanalyserbackend.dto.response.JobDescriptionResponseDto;
import com.example.airesumeanalyserbackend.services.job_description_service.JobDescriptionService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/web/api/jobDescription")
public class JobDescriptionController {
    private final JobDescriptionService jobDescriptionService;

    public JobDescriptionController(JobDescriptionService jobDescriptionService) {
        this.jobDescriptionService = jobDescriptionService;
    }

    @PostMapping("/v1/createJobDescription")
    public ResponseEntity<JobDescriptionResponseDto> createJobDescription(
            @RequestBody CreateJobDescriptionDto createJobDescriptionDto) {
        JobDescriptionResponseDto created = jobDescriptionService.createJobDescription(createJobDescriptionDto);
        return ResponseEntity.ok(created);
    }

    @GetMapping("/v1/displayJobById")
    public ResponseEntity<JobDescriptionResponseDto> getJobById(@RequestParam String jobId) {
        return ResponseEntity.ok(jobDescriptionService.getJobById(jobId));
    }

    @GetMapping("/v1/displayAllJobs")
    public ResponseEntity<List<JobDescriptionResponseDto>> getAllJobs() {
        return ResponseEntity.ok(jobDescriptionService.getAllJobs());
    }
}
