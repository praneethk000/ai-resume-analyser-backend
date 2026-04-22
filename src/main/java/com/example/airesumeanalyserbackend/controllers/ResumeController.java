package com.example.airesumeanalyserbackend.controllers;

import com.example.airesumeanalyserbackend.dto.request.CreateResumeDto;
import com.example.airesumeanalyserbackend.dto.request.UpdateResumeProfileDto;
import com.example.airesumeanalyserbackend.dto.request.UpdateSkillsDto;
import com.example.airesumeanalyserbackend.dto.response.ResumeResponseDto;
import com.example.airesumeanalyserbackend.dto.response.ResumeUploadResponseDto;
import com.example.airesumeanalyserbackend.models.Resume;
import com.example.airesumeanalyserbackend.services.resume_service.ResumeService;
import com.example.airesumeanalyserbackend.utils.PdfParsingUtil;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/web/api/resume")
public class ResumeController {

    private final ResumeService resumeService;

    public ResumeController(ResumeService resumeService) {
        this.resumeService = resumeService;
    }

    @PostMapping("/v1/createResume")
    public ResponseEntity<String> createResume(@RequestBody CreateResumeDto createResumeDto) {
        resumeService.createResume(createResumeDto);
        return ResponseEntity.ok("Resume created");
    }

    @GetMapping("/v1/displayResumeById")
    public ResponseEntity<ResumeResponseDto> getResume(@RequestParam("resumeId") String resumeId) {
        return ResponseEntity.ok(resumeService.getResumeById(resumeId));
    }

    @GetMapping("/v1/displayResumeByUser")
    public ResponseEntity<List<Resume>> getResumesByUser(@RequestParam("userId") String userId) {
        return ResponseEntity.ok(resumeService.getResumesByUser(userId));
    }

    @PutMapping("/v1/updateSkills")
    public ResponseEntity<String> updateSkills(@RequestBody UpdateSkillsDto updateSkillsDto) {
        resumeService.updateResumeSkills(updateSkillsDto);
        return ResponseEntity.ok("Skills updated successfully");
    }

    @PutMapping("/v1/updateProfile")
    public ResponseEntity<String> updateProfile(@RequestBody UpdateResumeProfileDto updateResumeProfileDto) {
        resumeService.updateResumeProfile(updateResumeProfileDto);
        return ResponseEntity.ok("Resume profile updated successfully");
    }

    @PostMapping(value = "/v1/uploadResume", consumes = { "multipart/form-data" })
    public ResponseEntity<ResumeUploadResponseDto> uploadResume(@RequestParam("file") MultipartFile file,
            @RequestParam("userId") String userId) {
        try {
            if (file.isEmpty()) {
                return ResponseEntity.badRequest().build();
            }

            ResumeUploadResponseDto response = resumeService.uploadAndExtractSkills(file, userId);

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

}
