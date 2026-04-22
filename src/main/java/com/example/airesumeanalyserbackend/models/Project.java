package com.example.airesumeanalyserbackend.models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "project")
public class Project {
    @Id
    private String projectId;

    @Column(nullable = false)
    private String projectName;

    @Column(nullable = false)
    private String projectDescription;

    @Lob
    @Column(nullable = false)
    private String technologiesUsed;

    @Column(nullable = false)
    private String projectLink;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "resume_id", nullable = false)
    @JsonBackReference(value = "resume-project")
    private Resume resume;
}
