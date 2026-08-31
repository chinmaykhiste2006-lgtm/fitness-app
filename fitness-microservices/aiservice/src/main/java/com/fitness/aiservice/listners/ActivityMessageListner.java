package com.fitness.aiservice.listners;

import org.springframework.stereotype.Component;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.kafka.annotation.KafkaListener;


import com.fitness.aiservice.model.Activity;
import com.fitness.aiservice.services.ActivityAIService;
import com.fitness.aiservice.services.RecommendationService;



@Component
@AllArgsConstructor
@Slf4j
public class ActivityMessageListner {

   private final ActivityAIService activityAIService;
   private final RecommendationService recommendationService;

    @KafkaListener(topics = "${kafka.topic.name}", groupId = "activity-processor-group")
    public void processActivity(Activity activity){
        log.info("Received activity for processing: {}", activity.getUserId());
        try{
            recommendationService.saveRecommendation(activityAIService.generateRecommendation(activity));
            log.info("Saved");
        }
        catch(Exception e){
            log.error("Error occurred while processing activity: {}", activity.getUserId(), e);
        }
    }
}
