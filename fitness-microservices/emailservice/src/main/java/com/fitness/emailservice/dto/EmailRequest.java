package com.fitness.emailservice.dto;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class EmailRequest {

    private String firstName;
    private String lastName;
    private String email;
    private LocalDateTime createdAt;

}
