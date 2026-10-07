package com.fitness.emailservice.listener;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import com.fitness.emailservice.dto.EmailRequest;
import com.fitness.emailservice.services.EmailService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@RequiredArgsConstructor
@Slf4j
public class UserEventListner {

    private final EmailService emailService;

    @KafkaListener(topics = "${kafka.topic.name}", groupId = "user-processor-group")
    public void processUserEvent(EmailRequest request) {
        log.info("Received user event for processing: {}", request.getEmail());
        try{
            emailService.sendEmail(request);
        } catch(Exception e){
            log.error("Error occurred while processing user event: {}", request.getEmail(), e);
        }
        
    }
}
