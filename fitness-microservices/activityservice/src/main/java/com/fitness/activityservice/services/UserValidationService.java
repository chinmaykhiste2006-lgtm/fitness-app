package com.fitness.activityservice.services;

import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;



import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class UserValidationService {

    private final WebClient userServiceWebClient;

     public boolean validateUser(String keycloakId) {
       
        return userServiceWebClient.get()
                .uri("/api/v1/users/validate")
                .header("keycloak-Id", keycloakId)
                .retrieve()
                .bodyToMono(Boolean.class)
                .block();
    }
  
}
