package com.example.airesumeanalyserbackend.repositories;

import com.example.airesumeanalyserbackend.models.JobDescription;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JobDescriptionRepository extends JpaRepository<JobDescription, String> {
}
