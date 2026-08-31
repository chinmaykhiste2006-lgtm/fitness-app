package com.fitness.emailservice.services;




import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import com.fitness.emailservice.dto.EmailRequest;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@AllArgsConstructor
@Slf4j
public class EmailService {

 
    private final JavaMailSender emailSender;

    public void sendEmail(EmailRequest request) {
        log.info("Sending email to: {}", request.getEmail());
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("chinmay.khiste241@vit.edu");
        message.setTo(request.getEmail());
        message.setSubject("Welcome to Fitness App");
        message.setText(buildWelcomeEmailBody(request));
        emailSender.send(message);
   
    }

   private String buildWelcomeEmailBody(EmailRequest request) {
    return String.format("""
            Hi %s %s,

            Welcome to Fitness App! Your account was created successfully on %s.

            We're excited to have you on board — track your workouts, monitor your progress, and reach your fitness goals with us.

            If you didn't create this account, please ignore this email or contact our support team.

            Thanks,
            The Fitness App Team
            """,
            request.getFirstName(),
            request.getLastName(),
            request.getCreatedAt()
    );
}
    
}
