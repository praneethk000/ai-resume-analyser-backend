package com.example.airesumeanalyserbackend.models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "resume")
public class Resume {
    @Id
    private String resumeId;

    @Column(nullable = false)
    private String title;

    @Column(nullable = true)
    private String firstName;

    @Column(nullable = true)
    private String lastName;

    @Column(nullable = true)
    private String phoneNumber;

    @Column(nullable = true)
    private String email;

    @Column(nullable = true)
    private String linkedin;

    @Column(nullable = true)
    private String github;

    @Column(nullable = true)
    private String address;

    @Column(nullable = true)
    private String city;

    @Column(nullable = true)
    private String state;

    @Column(nullable = true)
    private String pincode;

    @Column(nullable = true)
    private String nationality;

    @Enumerated(EnumType.STRING)
    @Column
    private Gender gender;

    @Column(nullable = true)
    private LocalDate dob;

    @Column(nullable = true)
    private String resumeUrl;

    @Column(nullable = true)
    private String resumeFileName;

    @Column(nullable = true)
    private LocalDateTime resumeUploadedAt;

    @Column(columnDefinition = "TEXT", nullable = true)
    @JsonIgnore
    private String resumeParsedText;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    @JsonBackReference(value = "user-resume")
    private User user;

    @OneToMany(mappedBy = "resume", cascade = CascadeType.ALL)
    @JsonManagedReference(value = "resume-education")
    private List<Education> educations = new ArrayList<>();

    @OneToMany(mappedBy = "resume", cascade = CascadeType.ALL)
    @JsonManagedReference(value = "resume-experience")
    private List<Experience> experiences = new ArrayList<>();

    @OneToMany(mappedBy = "resume", cascade = CascadeType.ALL)
    @JsonManagedReference(value = "resume-project")
    private List<Project> projects = new ArrayList<>();

    @OneToMany(mappedBy = "resume", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference(value = "resume-skill")
    private List<ResumeSkills> resumeSkills = new ArrayList<>();

    @OneToMany(mappedBy = "resume", cascade = CascadeType.ALL)
    @JsonManagedReference(value = "resume-analysis")
    private List<ResumeAnalysis> resumeAnalysis = new ArrayList<>();
}
