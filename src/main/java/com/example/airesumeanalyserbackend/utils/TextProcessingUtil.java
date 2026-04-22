package com.example.airesumeanalyserbackend.utils;


import java.util.Arrays;
import java.util.Collections;
import java.util.Set;
import java.util.stream.Collectors;


public class TextProcessingUtil {
    private static Set<String> STOP_WORDS = Set.of(
            "a", "an", "the", "is", "are", "was", "were", "in", "on", "at", "of", "for", "to", "with", "and", "or", "as", "by", "from"
    );

    public static Set<String> extractKeywords(String text) {
        if(text == null || text.isBlank()) {
            return Collections.emptySet();
        }
        return Arrays.stream(text.toLowerCase()
                .replaceAll("[^a-zA-Z0-9 ]", "")
                .split("\\s+"))
                .filter(word -> !word.isBlank())
                .filter(word -> !STOP_WORDS.contains(word))
                .collect(Collectors.toSet());
    }
}
