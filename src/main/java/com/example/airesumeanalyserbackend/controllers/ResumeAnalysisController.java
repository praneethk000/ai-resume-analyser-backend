package com.example.airesumeanalyserbackend.controllers;

import com.example.airesumeanalyserbackend.dto.response.ResumeAnalysisResponseDto;
import com.example.airesumeanalyserbackend.services.resume_analysis_service.ResumeAnalysisService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/web/api/resumeAnalysis")
public class ResumeAnalysisController {

    private final ResumeAnalysisService resumeAnalysisService;

    public ResumeAnalysisController(ResumeAnalysisService resumeAnalysisService) {
        this.resumeAnalysisService = resumeAnalysisService;
    }

    @PostMapping("/v1/analyseResume")
    public ResponseEntity<ResumeAnalysisResponseDto> analyseResume(@RequestParam String resumeId, @RequestParam String jobId){
        return ResponseEntity.ok(resumeAnalysisService.analyseResume(resumeId, jobId));
    }

    @GetMapping("/v1/displayAllAnalysisByResume")
    public ResponseEntity<List<ResumeAnalysisResponseDto>> displayAllAnalysisByResumeId(@RequestParam String resumeId) {
        return ResponseEntity.ok(resumeAnalysisService.getAnalysisByResumeId(resumeId));
    }

    @GetMapping("/v1/displayAllAnalysisByUser")
    public ResponseEntity<List<ResumeAnalysisResponseDto>> displayAllAnalysisByUserId(@RequestParam String userId) {
        return ResponseEntity.ok(resumeAnalysisService.getAnalysisByUserId(userId));
    }
}
