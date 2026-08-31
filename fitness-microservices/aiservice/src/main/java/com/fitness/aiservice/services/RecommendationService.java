package com.fitness.aiservice.services;

import java.util.List;

import org.springframework.stereotype.Service;

import com.fitness.aiservice.model.Answer;
import com.fitness.aiservice.model.CustomRecommendation;
import com.fitness.aiservice.model.Recommendation;
import com.fitness.aiservice.repositories.RecommendationRepository;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class RecommendationService {


    private final RecommendationRepository recommendationRepository;
    private final ActivityAIService activityAIService;



    public List<Recommendation> getUserRecommendations(String keycloakId) {
     
    return recommendationRepository.findByKeycloakId(keycloakId);
    }

    public Recommendation getActivityRecommendations(String activityId) {
        return recommendationRepository.findByActivityId(activityId).orElseThrow(() -> new RuntimeException("No recommendation found for this  activity: " + activityId));
    }

    public Recommendation saveRecommendation(Recommendation recommendation) {
        return recommendationRepository.save(recommendation);
    }

        public Answer generateAnswer(String question){
          return activityAIService.generateAnswer(question);   
    }

    public CustomRecommendation generateCustomRecommendation(String keycloakId) {
        
        return activityAIService.generateCustomRecommendation(keycloakId, getUserRecommendations(keycloakId));
    }
}
