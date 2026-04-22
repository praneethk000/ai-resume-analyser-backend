package com.example.airesumeanalyserbackend.repositories;

import com.example.airesumeanalyserbackend.models.Skill;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface SkillRepository extends JpaRepository<Skill, String> {
    Optional<Skill> findBySkillName(String skillName);
}
