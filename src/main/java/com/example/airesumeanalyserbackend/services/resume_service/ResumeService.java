package com.example.airesumeanalyserbackend.services.resume_service;

import com.example.airesumeanalyserbackend.dto.request.CreateResumeDto;
import com.example.airesumeanalyserbackend.dto.request.UpdateResumeProfileDto;
import com.example.airesumeanalyserbackend.dto.request.UpdateSkillsDto;
import com.example.airesumeanalyserbackend.dto.response.ResumeResponseDto;
import com.example.airesumeanalyserbackend.dto.response.ResumeUploadResponseDto;
import com.example.airesumeanalyserbackend.models.Resume;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

public interface ResumeService {
    String createResume(CreateResumeDto createResumeDto);

    ResumeResponseDto getResumeById(String resumeId);

    List<Resume> getResumesByUser(String userId);

    String updateResumeSkills(UpdateSkillsDto updateSkillsDto);

    String updateResumeProfile(UpdateResumeProfileDto updateResumeProfileDto);

    ResumeUploadResponseDto uploadAndExtractSkills(MultipartFile file, String userId) throws Exception;
}
