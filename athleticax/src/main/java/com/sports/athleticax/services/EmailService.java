package com.sports.athleticax.services;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.*;

@Service
public class EmailService {

    @Value("${brevo.api.key}")
    private String apiKey;

    @Value("${mail.from}")
    private String fromEmail;

    private final RestTemplate restTemplate = new RestTemplate();

    public void sendOtpEmail(String toEmail, String otp) {

        String url = "https://api.brevo.com/v3/smtp/email";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("api-key", apiKey);

        Map<String, Object> body = new HashMap<>();

        Map<String, String> sender = new HashMap<>();
        sender.put("name", "AthleticaX");
        sender.put("email", fromEmail);

        body.put("sender", sender);

        List<Map<String, String>> to = new ArrayList<>();

        Map<String, String> recipient = new HashMap<>();
        recipient.put("email", toEmail);

        to.add(recipient);

        body.put("to", to);

        body.put("subject", "AthleticaX OTP Verification");

        body.put(
                "textContent",
                "Dear User,\n\n"
                        + "Your OTP is: "
                        + otp
                        + "\n\n"
                        + "It is valid for 5 minutes."
                        + "\n\n"
                        + "Regards,\n"
                        + "AthleticaX"
        );

        HttpEntity<Map<String, Object>> request =
                new HttpEntity<>(body, headers);

        ResponseEntity<String> response =
                restTemplate.postForEntity(url, request, String.class);

        if (!response.getStatusCode().is2xxSuccessful()) {
            throw new RuntimeException("Failed to send email: " + response.getBody());
        }
    }
}
