package com.example.airesumeanalyserbackend.controllers;

import com.example.airesumeanalyserbackend.dto.request.CreateResumeDto;
import com.example.airesumeanalyserbackend.dto.request.UpdateResumeProfileDto;
import com.example.airesumeanalyserbackend.dto.request.UpdateSkillsDto;
import com.example.airesumeanalyserbackend.dto.response.ResumeResponseDto;
import com.example.airesumeanalyserbackend.dto.response.ResumeUploadResponseDto;
import com.example.airesumeanalyserbackend.exceptions.ApiRequestException;
import com.example.airesumeanalyserbackend.models.User;
import com.example.airesumeanalyserbackend.repositories.ResumeRepository;
import com.example.airesumeanalyserbackend.services.resume_service.ResumeService;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/web/api/resume")
public class ResumeController {

    private final ResumeService resumeService;
    private final ResumeRepository resumeRepository;

    public ResumeController(ResumeService resumeService, ResumeRepository resumeRepository) {
        this.resumeService = resumeService;
        this.resumeRepository = resumeRepository;
    }

    @PostMapping("/v1/createResume")
    public ResponseEntity<String> createResume(
            @RequestBody CreateResumeDto createResumeDto,
            @AuthenticationPrincipal User currentUser) {
        // IDOR: Prevent creating resumes for other users
        if (!currentUser.getUserId().equals(createResumeDto.userId())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Access denied");
        }
        resumeService.createResume(createResumeDto);
        return ResponseEntity.ok("Resume created");
    }

    @GetMapping("/v1/displayResumeById")
    public ResponseEntity<ResumeResponseDto> getResume(
            @RequestParam("resumeId") String resumeId,
            @AuthenticationPrincipal User currentUser) {
        // IDOR: Verify the resume belongs to the caller
        resumeRepository.findById(resumeId).ifPresent(resume -> {
            if (!resume.getUser().getUserId().equals(currentUser.getUserId())) {
                throw new ApiRequestException("Access denied");
            }
        });
        return ResponseEntity.ok(resumeService.getResumeById(resumeId));
    }

    @GetMapping("/v1/displayResumeByUser")
    public ResponseEntity<List<ResumeResponseDto>> getResumesByUser(
            @RequestParam("userId") String userId,
            @AuthenticationPrincipal User currentUser) {
        // IDOR: Only allow fetching your own resumes
        if (!currentUser.getUserId().equals(userId)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        return ResponseEntity.ok(resumeService.getResumesByUser(userId));
    }

    @PutMapping("/v1/updateSkills")
    public ResponseEntity<String> updateSkills(
            @RequestBody UpdateSkillsDto updateSkillsDto,
            @AuthenticationPrincipal User currentUser) {
        // IDOR: Verify the resume belongs to the caller before updating
        resumeRepository.findById(updateSkillsDto.resumeId()).ifPresent(resume -> {
            if (!resume.getUser().getUserId().equals(currentUser.getUserId())) {
                throw new ApiRequestException("Access denied");
            }
        });
        resumeService.updateResumeSkills(updateSkillsDto);
        return ResponseEntity.ok("Skills updated successfully");
    }

    @PutMapping("/v1/updateProfile")
    public ResponseEntity<String> updateProfile(
            @RequestBody UpdateResumeProfileDto updateResumeProfileDto,
            @AuthenticationPrincipal User currentUser) {
        // IDOR: Verify the resume belongs to the caller before updating
        resumeRepository.findById(updateResumeProfileDto.resumeId()).ifPresent(resume -> {
            if (!resume.getUser().getUserId().equals(currentUser.getUserId())) {
                throw new ApiRequestException("Access denied");
            }
        });
        resumeService.updateResumeProfile(updateResumeProfileDto);
        return ResponseEntity.ok("Resume profile updated successfully");
    }

    @PostMapping(value = "/v1/uploadResume", consumes = { "multipart/form-data" })
    public ResponseEntity<ResumeUploadResponseDto> uploadResume(
            @RequestParam("file") MultipartFile file,
            @AuthenticationPrincipal User currentUser) {
        try {
            // Validate file is not empty
            if (file.isEmpty()) {
                return ResponseEntity.badRequest().build();
            }
            // File type validation: only accept PDFs
            String contentType = file.getContentType();
            if (contentType == null || !contentType.equals("application/pdf")) {
                return ResponseEntity.status(HttpStatus.UNSUPPORTED_MEDIA_TYPE).build();
            }
            // Use the authenticated user's ID — ignoring any userId sent in the request body
            ResumeUploadResponseDto response = resumeService.uploadAndExtractSkills(file, currentUser.getUserId());
            return ResponseEntity.ok(response);
        } catch (ApiRequestException e) {
            throw e; // let GlobalExceptionHandler / ApiRequestExceptionHandler handle it
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @PostMapping(value = "/v1/extractSkillsOnly", consumes = { "multipart/form-data" })
    public ResponseEntity<List<String>> extractSkillsOnly(@RequestParam("file") MultipartFile file) {
        try {
            if (file.isEmpty()) {
                return ResponseEntity.badRequest().build();
            }
            // File type validation
            String contentType = file.getContentType();
            if (contentType == null || !contentType.equals("application/pdf")) {
                return ResponseEntity.status(HttpStatus.UNSUPPORTED_MEDIA_TYPE).build();
            }
            List<String> skills = resumeService.extractSkillsOnly(file);
            return ResponseEntity.ok(skills);
        } catch (ApiRequestException e) {
            throw e;
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }
}
