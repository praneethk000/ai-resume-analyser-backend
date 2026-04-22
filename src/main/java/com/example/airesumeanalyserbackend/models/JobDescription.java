package com.example.airesumeanalyserbackend.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "job_description")
public class JobDescription {
    @Id
    private String jobId;

    @Column(nullable = false)
    private String jobTitle;

    @Column(nullable = false)
    private String companyName;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String jobDescriptionText;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    @JsonIgnore
    @OneToMany(mappedBy = "jobDescription", cascade = CascadeType.ALL)
    @JsonManagedReference(value = "job-analysis")
    private List<ResumeAnalysis> resumeAnalysis = new ArrayList<>();
}
