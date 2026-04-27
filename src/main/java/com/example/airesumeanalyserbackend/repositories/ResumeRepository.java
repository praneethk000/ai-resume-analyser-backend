package com.example.airesumeanalyserbackend.repositories;

import com.example.airesumeanalyserbackend.models.Resume;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ResumeRepository extends JpaRepository<Resume, String> {
    @EntityGraph(attributePaths = {"resumeSkills", "resumeSkills.skill"})
    List<Resume> findResumesByUser_UserId(String userUserId);
}
