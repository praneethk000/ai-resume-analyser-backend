package com.example.airesumeanalyserbackend.models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "education")
public class Education {
    @Id
    private String educationId;

    @Column(nullable = false)
    private String institutionName;

    @Column(nullable = false)
    private String degree;

    @Column(nullable = false)
    private String major;

    @Column(nullable = false)
    private LocalDate startDate;

    @Column(nullable = true)
    private LocalDate endDate;

    @Column(nullable = false)
    private String grade;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "resume_id", nullable = false)
    @JsonBackReference(value = "resume-education")
    private Resume resume;
}
