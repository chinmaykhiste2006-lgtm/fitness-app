package com.fitness.aiservice.controllers;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fitness.aiservice.model.Answer;
import com.fitness.aiservice.model.CustomRecommendation;
import com.fitness.aiservice.model.Recommendation;
import com.fitness.aiservice.services.RecommendationService;

import lombok.AllArgsConstructor;



@RestController
@AllArgsConstructor
@RequestMapping("/api/v1/recommendations")
public class RecommendationController {

    private final RecommendationService recommendationService;

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Recommendation>> getUserRecommendations(@PathVariable String userId) {
        return ResponseEntity.ok(recommendationService.getUserRecommendations(userId));
    }

    @GetMapping("/activity/{activityId}")
    public ResponseEntity<Recommendation> getActivityRecommendations(@PathVariable String activityId) {
        return ResponseEntity.ok(recommendationService.getActivityRecommendations(activityId));
    }

    @GetMapping("/{question}")
    public ResponseEntity<Answer> getAnswer(@PathVariable String question) {
        return ResponseEntity.ok(recommendationService.generateAnswer(question));
    }

    @GetMapping("/recommend")
    public ResponseEntity<CustomRecommendation> getCustomRecommendation(@RequestHeader("keycloak-Id") String keycloakId) {      
        return ResponseEntity.ok(recommendationService.generateCustomRecommendation(keycloakId));
    }
}
