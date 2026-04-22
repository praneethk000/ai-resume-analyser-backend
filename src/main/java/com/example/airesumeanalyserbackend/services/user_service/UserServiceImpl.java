package com.example.airesumeanalyserbackend.services.user_service;

import com.example.airesumeanalyserbackend.dto.request.CreateUserDto;
import com.example.airesumeanalyserbackend.dto.response.ResumeResponseDto;
import com.example.airesumeanalyserbackend.dto.response.UserResponseDto;
import com.example.airesumeanalyserbackend.exceptions.ApiRequestException;
import com.example.airesumeanalyserbackend.models.User;
import com.example.airesumeanalyserbackend.repositories.UserRepository;
import com.example.airesumeanalyserbackend.utils.UUIDService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UUIDService uuidService;

    public UserServiceImpl(UserRepository userRepository, UUIDService uuidService) {
        this.userRepository = userRepository;
        this.uuidService = uuidService;
    }

    @Override
    public String createUser(CreateUserDto createUserDto) {
        if (createUserDto.email() == null || createUserDto.email().isBlank()) {
            throw new ApiRequestException("Email cannot be empty");
        }
        if (createUserDto.password() == null || createUserDto.password().isBlank()) {
            throw new ApiRequestException("Password cannot be empty");
        }
        if (userRepository.findByEmail(createUserDto.email()).isPresent()) {
            throw new ApiRequestException("Email already exists");
        }
        User user = new User();
        user.setUserId(uuidService.generateUUID());
        user.setUsername(createUserDto.username());
        user.setEmail(createUserDto.email());
        user.setPassword(createUserDto.password());
        user.setCreatedAt(LocalDateTime.now());
        userRepository.save(user);
        return "User created successfully";
    }

    @Override
    public UserResponseDto getUserById(String userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new ApiRequestException("User not found"));
        return new UserResponseDto(
                user.getUserId(),
                user.getUsername(),
                user.getEmail(),
                user.getResumes().stream()
                        .map(resume -> new ResumeResponseDto(
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
                                resume.getResumeSkills().stream().map(rs -> rs.getSkill().getSkillName()).toList()))
                        .toList());
    }

    @Override
    public List<UserResponseDto> getAllUsers() {
        List<User> users = userRepository.findAll();
        return users.stream().map(
                user -> new UserResponseDto(
                        user.getUserId(),
                        user.getUsername(),
                        user.getEmail(),
                        user.getResumes().stream()
                                .map(resume -> new ResumeResponseDto(
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
                                        resume.getResumeSkills().stream().map(rs -> rs.getSkill().getSkillName())
                                                .toList()))
                                .toList()))
                .toList();
    }
}
