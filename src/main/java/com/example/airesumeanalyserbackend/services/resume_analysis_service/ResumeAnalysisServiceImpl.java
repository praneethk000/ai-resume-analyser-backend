package com.example.airesumeanalyserbackend.services.resume_analysis_service;

import com.example.airesumeanalyserbackend.dto.response.ResumeAnalysisResponseDto;
import com.example.airesumeanalyserbackend.exceptions.ApiRequestException;
import com.example.airesumeanalyserbackend.models.JobDescription;
import com.example.airesumeanalyserbackend.models.Resume;
import com.example.airesumeanalyserbackend.models.ResumeAnalysis;
import com.example.airesumeanalyserbackend.repositories.JobDescriptionRepository;
import com.example.airesumeanalyserbackend.repositories.ResumeAnalysisRepository;
import com.example.airesumeanalyserbackend.repositories.ResumeRepository;
import com.example.airesumeanalyserbackend.services.ai_service.OpenAIService;
import com.example.airesumeanalyserbackend.utils.TextProcessingUtil;
import com.example.airesumeanalyserbackend.utils.UUIDService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class ResumeAnalysisServiceImpl implements ResumeAnalysisService {

    private final ResumeRepository resumeRepository;
    private final JobDescriptionRepository jobDescriptionRepository;
    private final ResumeAnalysisRepository resumeAnalysisRepository;
    private final UUIDService uuidService;
    private final OpenAIService openAIService;

    public ResumeAnalysisServiceImpl(ResumeRepository resumeRepository,
            JobDescriptionRepository jobDescriptionRepository, ResumeAnalysisRepository resumeAnalysisRepository,
            UUIDService uuidService, OpenAIService openAIService) {
        this.resumeRepository = resumeRepository;
        this.jobDescriptionRepository = jobDescriptionRepository;
        this.resumeAnalysisRepository = resumeAnalysisRepository;
        this.uuidService = uuidService;
        this.openAIService = openAIService;
    }

    @Override
    public ResumeAnalysisResponseDto analyseResume(String resumeId, String jobId) {

        Resume resume = resumeRepository.findById(resumeId)
                .orElseThrow(() -> new ApiRequestException("Resume not found"));
        JobDescription job = jobDescriptionRepository.findById(jobId)
                .orElseThrow(() -> new ApiRequestException("Job not found"));

        String resumeText = resume.getResumeParsedText();
        String jobText = job.getJobDescriptionText();
        if (resumeText == null || resumeText.isBlank()) {
            throw new ApiRequestException("Resume not found");
        }
        if (jobText == null || jobText.isBlank()) {
            throw new ApiRequestException("Job not found");
        }

        Set<String> resumeKeywords = resume.getResumeSkills().stream().map(rs -> rs.getSkill().getSkillName())
                .collect(Collectors.toSet());
        Set<String> jobKeywords = new HashSet<>(openAIService.extractSkills(job.getJobDescriptionText()));

        System.out.println("Final Resume Skills: " + resumeKeywords);
        System.out.println("Final Job Skills: " + jobKeywords);

        // 3. Match keywords
        Set<String> matched = new HashSet<>();
        for (String jobWord : jobKeywords) {
            String lowerJobWord = jobWord.toLowerCase();
            for (String resumeWord : resumeKeywords) {
                String lowerResumeWord = resumeWord.toLowerCase();
                if (lowerResumeWord.contains(lowerJobWord) || lowerJobWord.contains(lowerResumeWord)) {
                    matched.add(jobWord);
                    break;
                }
            }
        }

        // 4. Find missing keywords
        Set<String> missing = new HashSet<>(jobKeywords);
        missing.removeAll(matched);

        // 5. Calculate score
        double score = jobKeywords.isEmpty()
                ? 0
                : ((double) matched.size() / jobKeywords.size()) * 100;

        // 6. Generate suggestions
        String suggestions = generateSuggestions(missing);

        // 7. Save analysis
        ResumeAnalysis analysis = new ResumeAnalysis();

        analysis.setResumeAnalysisId(uuidService.generateUUID());
        analysis.setResume(resume);
        analysis.setJobDescription(job);
        analysis.setMatchScore((float) score);
        analysis.setMatchedSkills(String.join(", ", matched));
        analysis.setMissingSkills(String.join(", ", missing));
        analysis.setSuggestions(suggestions);
        analysis.setCreatedAt(LocalDateTime.now());

        ResumeAnalysis savedAnalysis = resumeAnalysisRepository.save(analysis);
        return new ResumeAnalysisResponseDto(
                savedAnalysis.getResumeAnalysisId(),
                savedAnalysis.getResume().getResumeId(),
                savedAnalysis.getJobDescription().getJobId(),
                savedAnalysis.getResume().getResumeFileName(),
                savedAnalysis.getJobDescription().getJobTitle(),
                savedAnalysis.getJobDescription().getCompanyName(),
                savedAnalysis.getMatchScore(),
                savedAnalysis.getMatchedSkills(),
                savedAnalysis.getMissingSkills(),
                savedAnalysis.getSuggestions(),
                savedAnalysis.getCreatedAt());
    }

    @Override
    public List<ResumeAnalysisResponseDto> getAnalysisByResumeId(String resumeId) {
        List<ResumeAnalysis> resumeAnalysis = resumeAnalysisRepository.findByResumeResumeId(resumeId);
        return resumeAnalysis.stream().map(
                analysis -> new ResumeAnalysisResponseDto(
                        analysis.getResumeAnalysisId(),
                        analysis.getResume().getResumeId(),
                        analysis.getJobDescription().getJobId(),
                        analysis.getResume().getResumeFileName(),
                        analysis.getJobDescription().getJobTitle(),
                        analysis.getJobDescription().getCompanyName(),
                        analysis.getMatchScore(),
                        analysis.getMatchedSkills(),
                        analysis.getMissingSkills(),
                        analysis.getSuggestions(),
                        analysis.getCreatedAt()))
                .toList();
    }

    @Override
    public List<ResumeAnalysisResponseDto> getAnalysisByUserId(String userId) {
        List<ResumeAnalysis> resumeAnalyses = resumeAnalysisRepository.findByResumeUserUserId(userId);
        return resumeAnalyses.stream().map(
                analysis -> new ResumeAnalysisResponseDto(
                        analysis.getResumeAnalysisId(),
                        analysis.getResume().getResumeId(),
                        analysis.getJobDescription().getJobId(),
                        analysis.getResume().getResumeFileName(),
                        analysis.getJobDescription().getJobTitle(),
                        analysis.getJobDescription().getCompanyName(),
                        analysis.getMatchScore(),
                        analysis.getMatchedSkills(),
                        analysis.getMissingSkills(),
                        analysis.getSuggestions(),
                        analysis.getCreatedAt()))
                .toList();
    }

    private String generateSuggestions(Set<String> missing) {
        if (missing.isEmpty()) {
            return "Your resume matches well with the job description.";
        }
        return "Consider adding these skills: " + String.join(", ", missing);
    }
}
