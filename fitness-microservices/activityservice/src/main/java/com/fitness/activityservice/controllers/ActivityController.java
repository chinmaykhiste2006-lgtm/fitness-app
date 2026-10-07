package com.fitness.activityservice.controllers;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fitness.activityservice.dto.ActivityRequest;
import com.fitness.activityservice.dto.ActivityResponse;
import com.fitness.activityservice.services.ActivityService;

import lombok.AllArgsConstructor;

@RestController
@RequestMapping("/api/v1/activities")
@AllArgsConstructor
public class ActivityController {

private final ActivityService activityService;

@PostMapping("/create")
public ResponseEntity<ActivityResponse> trackActivity(@RequestBody ActivityRequest request, @RequestHeader("keycloak-Id") String keycloakId) {
    
    return ResponseEntity.ok(activityService.trackActivity(request, keycloakId));
}

@GetMapping("/{activityId}")
public ResponseEntity<ActivityResponse> getActivityById(@PathVariable String activityId, @RequestHeader("keycloak-Id") String keycloakId) {
    return ResponseEntity.ok(activityService.getActivityById(activityId, keycloakId));
}

@GetMapping("/search")
public ResponseEntity<List<ActivityResponse>> searchActivities(@RequestParam String keyword) {
    return ResponseEntity.ok(activityService.searchActivities(keyword));
}
}
