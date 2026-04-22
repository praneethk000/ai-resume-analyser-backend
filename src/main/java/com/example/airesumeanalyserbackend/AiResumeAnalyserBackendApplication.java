package com.example.airesumeanalyserbackend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@EnableCaching
@SpringBootApplication
public class AiResumeAnalyserBackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(AiResumeAnalyserBackendApplication.class, args);
    }

}
