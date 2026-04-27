package com.example.airesumeanalyserbackend.repositories;

import com.example.airesumeanalyserbackend.models.ResumeAnalysis;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ResumeAnalysisRepository extends JpaRepository<ResumeAnalysis, String> {
    @EntityGraph(attributePaths = {"resume", "jobDescription"})
    List<ResumeAnalysis> findByResumeResumeId(String resumeResumeId);

    @EntityGraph(attributePaths = {"resume", "jobDescription"})
    List<ResumeAnalysis> findByResumeUserUserId(String resumeUserUserId);
}
