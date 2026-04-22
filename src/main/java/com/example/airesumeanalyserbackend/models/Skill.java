package com.example.airesumeanalyserbackend.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "skill")
public class Skill {
    @Id
    private String skillId;

    @Column(unique = true, nullable = false)
    private String skillName;

    @OneToMany(mappedBy = "skill", cascade = CascadeType.ALL)
    private List<ResumeSkills> resumeSkills = new ArrayList<>();
}
