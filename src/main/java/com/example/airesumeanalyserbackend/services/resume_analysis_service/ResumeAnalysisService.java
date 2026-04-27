package com.example.airesumeanalyserbackend.services.resume_analysis_service;

import com.example.airesumeanalyserbackend.dto.response.ResumeAnalysisResponseDto;
import com.example.airesumeanalyserbackend.models.ResumeAnalysis;

import java.util.List;

public interface ResumeAnalysisService {
    ResumeAnalysisResponseDto analyseResume(String resumeId, String jobId);
    List<ResumeAnalysisResponseDto> getAnalysisByResumeId(String resumeId);
    List<ResumeAnalysisResponseDto> getAnalysisByUserId(String userId);
}
