package com.fitness.activityservice.services;




import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import com.fitness.activityservice.dto.ActivityRequest;
import com.fitness.activityservice.dto.ActivityResponse;
import com.fitness.activityservice.model.Activity;
import com.fitness.activityservice.repositories.ActivityRepository;
import java.util.List;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;



@Service
@RequiredArgsConstructor
@Slf4j
public class ActivityService {

    private final ActivityRepository activityRepository;

    private final UserValidationService userValidationService;

    private final KafkaTemplate<String, Activity> kafkaTemplate;

    @Value("${kafka.topic.name}")
    private String topicName;

    public ActivityResponse trackActivity(ActivityRequest request, String keycloakId) {


        request.setUserId(keycloakId);
       boolean isValidUser = userValidationService.validateUser(keycloakId);

       if(!isValidUser){
            throw new RuntimeException("Invalid user: " + request.getUserId());
        }
       Activity activity = mapToEntity(request, keycloakId);

        Activity savedActivity = activityRepository.save(activity);
    
        try{

            kafkaTemplate.send(topicName, savedActivity.getUserId(), savedActivity);
            log.info("sent event to kafka");
        } catch (Exception e) {
           e.printStackTrace();
        }
        return mapToResponse(savedActivity); 
    }

    public ActivityResponse getActivityById(String activityId, String keycloakId) {

       
        Activity activity = activityRepository.findById(activityId)
                .orElseThrow(() -> new RuntimeException("Activity not found with id: " + activityId));

        if (!activity.getUserId().equals(keycloakId)) {
            log.warn("User {} attempted to access activity {} owned by {}", keycloakId, activityId, activity.getUserId());
            throw new RuntimeException("Activity does not belong to the user");
        }
             
        return mapToResponse(activity);
    }

    public ActivityResponse updateActivity(String activityId, ActivityRequest request) {
        Activity existingActivity = activityRepository.findById(activityId)
                .orElseThrow(() -> new RuntimeException("Activity not found with id: " + activityId));

        existingActivity.setType(request.getType());
        existingActivity.setDuration(request.getDuration());
        existingActivity.setCaloriesBurned(request.getCaloriesBurned());
        existingActivity.setStartTime(request.getStartTime());
        existingActivity.setAdditionalMetrics(request.getAdditionalMetrics());

        Activity updatedActivity = activityRepository.save(existingActivity);
        return mapToResponse(updatedActivity);
    }

   public List<ActivityResponse> searchActivities(String keyword) {
    return activityRepository.findByKeywords(keyword).stream()
            .map(this::mapToResponse)
            .toList();
            
}

    private ActivityResponse mapToResponse(Activity activity) {
        return new ActivityResponse(
                activity.getId(),
                activity.getUserId(),
                activity.getType().name(),
                activity.getDuration(),
                activity.getCaloriesBurned(),
                activity.getStartTime(),
                activity.getAdditionalMetrics(),
                activity.getCreatedAt(),
                activity.getUpdatedAt()
        );
    }

    private Activity mapToEntity(ActivityRequest request, String keycloakId) {

        return Activity.builder()
                .userId(request.getUserId())
                .type(request.getType())
                .duration(request.getDuration())
                .caloriesBurned(request.getCaloriesBurned())
                .startTime(request.getStartTime())
                .additionalMetrics(request.getAdditionalMetrics())
                .build();
    }

}
