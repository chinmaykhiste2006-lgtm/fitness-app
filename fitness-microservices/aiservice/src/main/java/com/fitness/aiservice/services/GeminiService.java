package com.fitness.aiservice.services;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;





@Service
@Slf4j
public class GeminiService {



    private final ChatClient chatClient;

    @Value("${spring.ai.google.genai.api-key}")
    private String geminiApiKey;

    public GeminiService(ChatClient.Builder chatClientBuilder) {
        this.chatClient = chatClientBuilder.build();
    }

    

    public String getRecommendations(String details) {
        
        log.info("Received request");
        String jsonResponse = chatClient.prompt()
                .user(details)
                .call()
                .content();

            return jsonResponse;
      
       
    }





}
