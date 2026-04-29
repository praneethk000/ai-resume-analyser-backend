package com.example.airesumeanalyserbackend.services.ai_service;

import com.example.airesumeanalyserbackend.exceptions.ApiRequestException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

@Service
public class OpenAIServiceImpl implements OpenAIService {
    private final WebClient webClient;
    // private final String apiKey; // DEAD FIELD — key is already baked into WebClient headers above; keeping it would hold the secret in memory unnecessarily

    public OpenAIServiceImpl(@Value("${github.models.api.key}") String apiKey) {
        this.webClient = WebClient.builder()
                .baseUrl("https://models.github.ai/inference/chat/completions")
                .defaultHeader("Content-Type", "application/json")
                .defaultHeader("Authorization", "Bearer " + apiKey)
                .build();
        // this.apiKey = apiKey; // not stored — key only lives in the WebClient instance
    }

    @Cacheable(value = "openaiSkills", key = "#text.hashCode()")
    @Override
    @SuppressWarnings("unchecked")
    public List<String> extractSkills(String text) {

        String prompt = """
                        You are an expert technical recruiter and AI resume analyzer.
                        Extract all relevant professional skills from the text below.
                        
                        Strict Rules:
                        1. Include hard technical skills (e.g., Java, Python, React, Next.js, AWS, MySQL).
                        2. Include methodologies and professional soft skills (e.g., Agile, Scrum, Problem Solving, Leadership, System Design).
                        3. STRICTLY EXCLUDE stop words, conversational words, verbs, and generic non-skill terms (e.g., 'we', 'experience', 'looking', 'years', 'team', 'good', 'strong', 'working', 'using').
                        4. Extract only the exact skills mentioned in the text. Do not invent skills.
                        5. Return ONLY a single comma-separated list of the extracted skills. Absolutely no other text, markdown, or explanation.

                        Text:
                        """ + text;

        Map<String, Object> requestBody = Map.of(
                "model", "gpt-4o-mini",
                "messages", List.of(
                        Map.of("role", "user", "content", prompt)
                )
        );

        Map<String, Object> response = webClient.post()
                .bodyValue(requestBody)
                .retrieve()
                .bodyToMono(Map.class)
                .doOnError(WebClientResponseException.class, ex -> {
                    // Intentionally not logging the full error body to avoid leaking API details
                })
                .onErrorMap(WebClientResponseException.class, ex -> {
                    if (ex.getStatusCode().value() == 429) {
                        return new ApiRequestException("OpenAI Rate Limit/Quota Exceeded: "
                                + ex.getResponseBodyAsString());
                    }
                    return new ApiRequestException("OpenAI API Error: " + ex.getMessage());
                })
                .block();

        if (response == null || !response.containsKey("choices")) {
            throw new ApiRequestException("Failed to get a valid response from OpenAI API");
        }

        List<Map<String, Object>> choices = (List<Map<String, Object>>) response.get("choices");
        Map<String, Object> message = (Map<String, Object>) choices.get(0).get("message");
        String content = (String) message.get("content");

        String extracted = content.toLowerCase();

        return Arrays.stream(extracted
                        .replaceAll("[^a-zA-Z0-9, \\+#\\.-]", "")
                        .split(","))
                        .map(String::trim)
                        .filter(skill -> !skill.isEmpty())
                        .toList();
    }
}
