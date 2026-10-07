package com.fitness.userservice.dto;

import java.time.LocalDateTime;


import com.fitness.userservice.models.UserRole;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserResponse {

    private String Id;
    private String keycloakId;
    private String firstName;
    private String lastName;
    private String password;
    private String email;
    private UserRole role = UserRole.USER;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt; 
}
