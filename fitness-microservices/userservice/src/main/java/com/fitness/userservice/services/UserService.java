package com.fitness.userservice.services;



import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import com.fitness.userservice.dto.EmailRequest;
import com.fitness.userservice.dto.RegisterRequest;
import com.fitness.userservice.dto.UserResponse;
import com.fitness.userservice.models.User;
import com.fitness.userservice.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;


@Service
@RequiredArgsConstructor
@Slf4j
public class UserService{

    private final UserRepository userRepository;
    private final KafkaTemplate<String, EmailRequest> kafkaTemplate;

    @Value("${kafka.topic.name}")
    private String topicName;
     
    
    public UserResponse register(RegisterRequest request, String keycloakId, String firstName, String lastName, String email) {

        log.info("request for user registration received");
        if(request == null){
            request = new RegisterRequest(firstName, lastName, email, "defaultPassword");
        }
        
        String userEmail = request.getEmail() != null ? request.getEmail() : email;
        
        if(userRepository.existsByEmail(userEmail)){
            
            User user = userRepository.findByEmail(userEmail);
            return mapToUserResponse(user);
        }

        User user = new User();
        user.setFirstName(request.getFirstName()!=null ? request.getFirstName() : firstName);
        user.setLastName(request.getLastName()!=null ? request.getLastName() : lastName);
        user.setEmail(request.getEmail()!=null ? request.getEmail() : email);
        user.setPassword(request.getPassword()!=null ? request.getPassword() : "defaultPassword");
        user.setKeycloakId(keycloakId);

        User savedUser = userRepository.save(user);
        kafkaTemplate.send(topicName, savedUser.getEmail(), mapToEmailRequest(savedUser));
        log.info("sent event to kafka for email");
        return mapToUserResponse(savedUser);
    }


    private  EmailRequest mapToEmailRequest(User savedUser) {
        return new EmailRequest(
            savedUser.getFirstName(),
            savedUser.getLastName(),
            savedUser.getEmail(),
            savedUser.getCreatedAt()
        );
    }


    public UserResponse getUserById(String userId) {
        
        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
        return mapToUserResponse(user);
       
    }

    public Boolean existsByKeycloakId(String keycloakId) {
        return userRepository.existsByKeycloakId(keycloakId);
    }

    private User mapToEntity(RegisterRequest request) {
        User user = new User();
        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        user.setEmail(request.getEmail());
        user.setPassword(request.getPassword());
        return user;
    }

    private UserResponse mapToUserResponse(User user) {
        UserResponse response = new UserResponse();
        response.setId(user.getId());
        response.setFirstName(user.getFirstName());
        response.setLastName(user.getLastName());
        response.setEmail(user.getEmail());
        response.setKeycloakId(user.getKeycloakId());
        response.setPassword(user.getPassword());
        response.setCreatedAt(user.getCreatedAt());
        response.setUpdatedAt(user.getUpdatedAt());
        return response;
    }


   
}

