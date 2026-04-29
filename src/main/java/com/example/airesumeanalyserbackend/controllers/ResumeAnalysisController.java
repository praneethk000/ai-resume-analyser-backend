package com.example.airesumeanalyserbackend.controllers;

import com.example.airesumeanalyserbackend.dto.response.ResumeAnalysisResponseDto;
import com.example.airesumeanalyserbackend.exceptions.ApiRequestException;
import com.example.airesumeanalyserbackend.models.User;
import com.example.airesumeanalyserbackend.repositories.ResumeRepository;
import com.example.airesumeanalyserbackend.services.resume_analysis_service.ResumeAnalysisService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/web/api/resumeAnalysis")
public class ResumeAnalysisController {

    private final ResumeAnalysisService resumeAnalysisService;
    private final ResumeRepository resumeRepository;

    public ResumeAnalysisController(ResumeAnalysisService resumeAnalysisService,
                                    ResumeRepository resumeRepository) {
        this.resumeAnalysisService = resumeAnalysisService;
        this.resumeRepository = resumeRepository;
    }

    @PostMapping("/v1/analyseResume")
    public ResponseEntity<ResumeAnalysisResponseDto> analyseResume(
            @RequestParam String resumeId,
            @RequestParam String jobId,
            @AuthenticationPrincipal User currentUser) {
        // IDOR: verify the resume being analysed belongs to the caller
        resumeRepository.findById(resumeId).ifPresent(resume -> {
            if (!resume.getUser().getUserId().equals(currentUser.getUserId())) {
                throw new ApiRequestException("Access denied");
            }
        });
        return ResponseEntity.ok(resumeAnalysisService.analyseResume(resumeId, jobId));
    }

    @GetMapping("/v1/displayAllAnalysisByResume")
    public ResponseEntity<List<ResumeAnalysisResponseDto>> displayAllAnalysisByResumeId(
            @RequestParam String resumeId,
            @AuthenticationPrincipal User currentUser) {
        // IDOR: verify the resume belongs to the caller
        resumeRepository.findById(resumeId).ifPresent(resume -> {
            if (!resume.getUser().getUserId().equals(currentUser.getUserId())) {
                throw new ApiRequestException("Access denied");
            }
        });
        return ResponseEntity.ok(resumeAnalysisService.getAnalysisByResumeId(resumeId));
    }

    @GetMapping("/v1/displayAllAnalysisByUser")
    public ResponseEntity<List<ResumeAnalysisResponseDto>> displayAllAnalysisByUserId(
            @RequestParam String userId,
            @AuthenticationPrincipal User currentUser) {
        // IDOR: only allow fetching your own analysis history
        if (!currentUser.getUserId().equals(userId)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        return ResponseEntity.ok(resumeAnalysisService.getAnalysisByUserId(userId));
    }
}
