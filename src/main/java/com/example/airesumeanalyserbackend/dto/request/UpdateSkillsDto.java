package com.example.airesumeanalyserbackend.dto.request;

import java.util.List;

public record UpdateSkillsDto(String resumeId, List<String> skills) {

}
