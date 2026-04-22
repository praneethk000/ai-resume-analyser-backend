package com.example.airesumeanalyserbackend.services.job_description_service;

import com.example.airesumeanalyserbackend.dto.request.CreateJobDescriptionDto;
import com.example.airesumeanalyserbackend.dto.response.JobDescriptionResponseDto;
import com.example.airesumeanalyserbackend.exceptions.ApiRequestException;
import com.example.airesumeanalyserbackend.models.JobDescription;
import com.example.airesumeanalyserbackend.repositories.JobDescriptionRepository;
import com.example.airesumeanalyserbackend.utils.UUIDService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class JobDescriptionServiceImpl implements JobDescriptionService{

    private final UUIDService uuidService;
    private final JobDescriptionRepository jobDescriptionRepository;

    public JobDescriptionServiceImpl(UUIDService uuidService, JobDescriptionRepository jobDescriptionRepository) {
        this.uuidService = uuidService;
        this.jobDescriptionRepository = jobDescriptionRepository;
    }

    @Override
    public String createJobDescription(CreateJobDescriptionDto jobDescriptionDto) {
        if(jobDescriptionDto.jobDescriptionText() == null || jobDescriptionDto.jobDescriptionText().isBlank()){
            throw new ApiRequestException("Job description cannot be empty");
        }
        if (jobDescriptionDto.jobTitle() == null || jobDescriptionDto.jobTitle().isBlank()) {
            throw new ApiRequestException("Job title cannot be empty");
        }
        if (jobDescriptionDto.companyName() == null || jobDescriptionDto.companyName().isBlank()) {
            throw new ApiRequestException("Company name cannot be empty");
        }
        JobDescription job = new JobDescription();

        job.setJobId(uuidService.generateUUID());
        job.setJobTitle(jobDescriptionDto.jobTitle());
        job.setCompanyName(jobDescriptionDto.companyName());
        job.setJobDescriptionText(jobDescriptionDto.jobDescriptionText());
        job.setCreatedAt(LocalDateTime.now());
        jobDescriptionRepository.save(job);
        return "Job Description Created Successfully";
    }

    @Override
    public JobDescriptionResponseDto getJobById(String jobId) {
        JobDescription job = jobDescriptionRepository.findById(jobId).orElseThrow(() -> new ApiRequestException("Job Description not found"));
        return new JobDescriptionResponseDto(
                job.getJobId(),
                job.getJobTitle(),
                job.getCompanyName(),
                job.getJobDescriptionText(),
                job.getCreatedAt()
        );
    }

    @Override
    public List<JobDescriptionResponseDto> getAllJobs() {
        return jobDescriptionRepository.findAll()
                .stream()
                .map(job -> new JobDescriptionResponseDto(
                        job.getJobId(),
                        job.getJobTitle(),
                        job.getCompanyName(),
                        job.getJobDescriptionText(),
                        job.getCreatedAt()
                )).toList();
    }
}
