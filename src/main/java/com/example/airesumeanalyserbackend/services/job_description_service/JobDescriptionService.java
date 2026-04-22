package com.example.airesumeanalyserbackend.services.job_description_service;

import com.example.airesumeanalyserbackend.dto.request.CreateJobDescriptionDto;
import com.example.airesumeanalyserbackend.dto.response.JobDescriptionResponseDto;

import java.util.List;

public interface JobDescriptionService {
    String createJobDescription(CreateJobDescriptionDto jobDescriptionDto);
    JobDescriptionResponseDto getJobById(String jobId);
    List<JobDescriptionResponseDto> getAllJobs();

}
