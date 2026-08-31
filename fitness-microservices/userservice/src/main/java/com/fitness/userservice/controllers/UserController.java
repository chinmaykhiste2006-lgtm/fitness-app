package com.fitness.userservice.controllers;


import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fitness.userservice.dto.RegisterRequest;
import com.fitness.userservice.dto.UserResponse;
import com.fitness.userservice.services.UserService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;

@RestController
@AllArgsConstructor
@RequestMapping("/api/v1/users")
public class UserController {

    private final UserService userService;

    @GetMapping("/")
    public String welcome(){
        return "Welcome to User Service";
    }

    @GetMapping("/{userId}")
    public ResponseEntity<UserResponse> getUserById( @Valid@PathVariable String userId){

        return ResponseEntity.ok(userService.getUserById(userId));
    }
    
    @PostMapping("/register")
    public ResponseEntity<UserResponse> register(@RequestBody(required = false) RegisterRequest request, 
                                                 @RequestHeader("keycloak-Id") String keycloakId, 
                                                 @RequestHeader("given-name") String firstName,
                                                 @RequestHeader("family-name") String lastName, 
                                                 @RequestHeader("email") String email) {

            

        return ResponseEntity.ok(userService.register(request, keycloakId, firstName, lastName, email));
    }

    @GetMapping("/validate")
    public ResponseEntity<Boolean> validateUser(@RequestHeader("keycloak-Id") String keycloakId) {
        return ResponseEntity.ok(userService.existsByKeycloakId(keycloakId));
    }

    
}
