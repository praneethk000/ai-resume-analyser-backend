package com.example.airesumeanalyserbackend.services.resume_service;

import com.example.airesumeanalyserbackend.dto.request.CreateResumeDto;
import com.example.airesumeanalyserbackend.dto.request.UpdateResumeProfileDto;
import com.example.airesumeanalyserbackend.dto.request.UpdateSkillsDto;
import com.example.airesumeanalyserbackend.dto.response.ResumeResponseDto;
import com.example.airesumeanalyserbackend.dto.response.ResumeUploadResponseDto;
import com.example.airesumeanalyserbackend.exceptions.ApiRequestException;
import com.example.airesumeanalyserbackend.models.Resume;
import com.example.airesumeanalyserbackend.models.ResumeSkills;
import com.example.airesumeanalyserbackend.models.Skill;
import com.example.airesumeanalyserbackend.models.User;
import com.example.airesumeanalyserbackend.repositories.ResumeRepository;
import com.example.airesumeanalyserbackend.repositories.SkillRepository;
import com.example.airesumeanalyserbackend.repositories.UserRepository;
import com.example.airesumeanalyserbackend.services.ai_service.OpenAIServiceImpl;
import com.example.airesumeanalyserbackend.utils.PdfParsingUtil;
import com.example.airesumeanalyserbackend.utils.UUIDService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class ResumeServiceImpl implements ResumeService {

    private final ResumeRepository resumeRepository;
    private final UserRepository userRepository;
    private final UUIDService uuidService;
    private final OpenAIServiceImpl openAiService;
    private final SkillRepository skillRepository;

    public ResumeServiceImpl(ResumeRepository resumeRepository, UserRepository userRepository, UUIDService uuidService,
            OpenAIServiceImpl openAiService, SkillRepository skillRepository) {
        this.resumeRepository = resumeRepository;
        this.userRepository = userRepository;
        this.uuidService = uuidService;
        this.openAiService = openAiService;
        this.skillRepository = skillRepository;
    }

    @Override
    public String createResume(CreateResumeDto createResumeDto) {
        User user = userRepository.findById(createResumeDto.userId())
                .orElseThrow(() -> new ApiRequestException("User not found"));

        if (createResumeDto.resumeParsedText() == null || createResumeDto.resumeParsedText().isBlank()) {
            throw new ApiRequestException("Resume parsed text cannot be empty");
        }
        Resume resume = new Resume();
        resume.setResumeId(uuidService.generateUUID());
        resume.setUser(user);
        resume.setTitle(createResumeDto.title());
        resume.setFirstName(createResumeDto.firstName());
        resume.setLastName(createResumeDto.lastName());
        resume.setPhoneNumber(createResumeDto.phoneNumber());
        resume.setEmail(createResumeDto.email());
        resume.setLinkedin(createResumeDto.linkedin());
        resume.setGithub(createResumeDto.github());
        resume.setAddress(createResumeDto.address());
        resume.setCity(createResumeDto.city());
        resume.setState(createResumeDto.state());
        resume.setPincode(createResumeDto.pincode());
        resume.setNationality(createResumeDto.nationality());
        resume.setGender(createResumeDto.gender());
        resume.setDob(createResumeDto.dob());
        resume.setResumeUrl(createResumeDto.resumeUrl());
        resume.setResumeFileName(createResumeDto.resumeFileName());
        resume.setResumeParsedText(createResumeDto.resumeParsedText());
        resume.setResumeUploadedAt(LocalDateTime.now());
        resumeRepository.save(resume);
        return "Resume created successfully";
    }

    @Override
    public ResumeResponseDto getResumeById(String resumeId) {
        Resume resume = resumeRepository.findById(resumeId)
                .orElseThrow(() -> new ApiRequestException("Resume not found"));
        List<String> skillsList = resume.getResumeSkills().stream().map(rs -> rs.getSkill().getSkillName()).toList();
        return new ResumeResponseDto(
                resume.getResumeId(),
                resume.getTitle(),
                resume.getFirstName(),
                resume.getLastName(),
                resume.getPhoneNumber(),
                resume.getEmail(),
                resume.getLinkedin(),
                resume.getGithub(),
                resume.getAddress(),
                resume.getCity(),
                resume.getState(),
                resume.getPincode(),
                resume.getNationality(),
                resume.getGender() != null ? resume.getGender().name() : null,
                resume.getDob(),
                resume.getResumeUrl(),
                resume.getResumeFileName(),
                skillsList);
    }

    @Override
    public List<ResumeResponseDto> getResumesByUser(String userId) {
        return resumeRepository.findResumesByUser_UserId(userId).stream()
                .map(resume -> {
                    List<String> skillsList = resume.getResumeSkills().stream()
                            .map(rs -> rs.getSkill().getSkillName())
                            .toList();
                    return new ResumeResponseDto(
                            resume.getResumeId(),
                            resume.getTitle(),
                            resume.getFirstName(),
                            resume.getLastName(),
                            resume.getPhoneNumber(),
                            resume.getEmail(),
                            resume.getLinkedin(),
                            resume.getGithub(),
                            resume.getAddress(),
                            resume.getCity(),
                            resume.getState(),
                            resume.getPincode(),
                            resume.getNationality(),
                            resume.getGender() != null ? resume.getGender().name() : null,
                            resume.getDob(),
                            resume.getResumeUrl(),
                            resume.getResumeFileName(),
                            skillsList);
                })
                .toList();
    }

    @Override
    public ResumeUploadResponseDto uploadAndExtractSkills(MultipartFile file, String userId) throws Exception {
        User user = userRepository.findById(userId).orElseThrow(() -> new ApiRequestException("User not found."));

        String parsedText = PdfParsingUtil.extractTextFromPdf(file);

        List<String> aiSkills = openAiService.extractSkills(parsedText);

        Set<String> mergedSkills = new HashSet<>(aiSkills);

        Resume resume = new Resume();
        resume.setResumeId(uuidService.generateUUID());
        resume.setUser(user);
        resume.setTitle("Resume");
        resume.setResumeFileName(file.getOriginalFilename());
        resume.setResumeParsedText(parsedText);
        resume.setResumeUploadedAt(LocalDateTime.now());

        for (String skillName : mergedSkills) {
            Skill skillEntity = skillRepository.findBySkillName(skillName).orElseGet(
                    () -> {
                        Skill newSkill = new Skill();
                        newSkill.setSkillId(uuidService.generateUUID());
                        newSkill.setSkillName(skillName);
                        return skillRepository.save(newSkill);
                    });
            ResumeSkills resumeSkill = new ResumeSkills();
            resumeSkill.setResumeSkillId(uuidService.generateUUID());
            resumeSkill.setResume(resume);
            resumeSkill.setSkill(skillEntity);

            resume.getResumeSkills().add(resumeSkill);
        }
        resumeRepository.save(resume);
        return new ResumeUploadResponseDto(resume.getResumeId(), new ArrayList<>(mergedSkills));
    }

    @Transactional
    @Override
    public String updateResumeSkills(UpdateSkillsDto updateSkillsDto) {
        Resume resume = resumeRepository.findById(updateSkillsDto.resumeId())
                .orElseThrow(() -> new ApiRequestException("Resume not found."));

        resume.getResumeSkills().clear();

        for (String skillName : updateSkillsDto.skills()) {
            Skill skillEntity = skillRepository.findBySkillName(skillName).orElseGet(
                    () -> {
                        Skill newSkill = new Skill();
                        newSkill.setSkillId(uuidService.generateUUID());
                        newSkill.setSkillName(skillName);
                        return skillRepository.save(newSkill);
                    });
            ResumeSkills resumeSkills = new ResumeSkills();
            resumeSkills.setResumeSkillId(uuidService.generateUUID());
            resumeSkills.setResume(resume);
            resumeSkills.setSkill(skillEntity);
            resume.getResumeSkills().add(resumeSkills);
        }
        resumeRepository.save(resume);
        return "Skills updated successfully";

    }

    @Override
    public String updateResumeProfile(UpdateResumeProfileDto updateResumeProfileDto) {
        Resume resume = resumeRepository.findById(updateResumeProfileDto.resumeId())
                .orElseThrow(() -> new ApiRequestException("Profile not found."));
        resume.setFirstName(updateResumeProfileDto.firstName());
        resume.setLastName(updateResumeProfileDto.lastName());
        resume.setPhoneNumber(updateResumeProfileDto.phoneNumber());
        resume.setEmail(updateResumeProfileDto.email());
        resume.setLinkedin(updateResumeProfileDto.linkedin());
        resume.setGithub(updateResumeProfileDto.github());
        resume.setAddress(updateResumeProfileDto.address());
        resume.setCity(updateResumeProfileDto.city());
        resume.setState(updateResumeProfileDto.state());
        resume.setPincode(updateResumeProfileDto.pincode());
        resume.setNationality(updateResumeProfileDto.nationality());
        resume.setDob(updateResumeProfileDto.dob());
        resume.setGender(updateResumeProfileDto.gender());
        resumeRepository.save(resume);
        return "Profile updated successfully";
    }
}
