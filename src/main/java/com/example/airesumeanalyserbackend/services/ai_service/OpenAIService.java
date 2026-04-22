package com.example.airesumeanalyserbackend.services.ai_service;

import java.util.List;

public interface OpenAIService {
    List<String> extractSkills(String text);
}
