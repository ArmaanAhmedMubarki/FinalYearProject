package com.sports.athleticax.services;

import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@Service
public class OllamaService {

    public String askOllama(String squadData) {

        RestTemplate restTemplate = new RestTemplate();

        String prompt =
                "Explain this Indian ODI squad selection:\n\n"
                + squadData;

        Map<String, Object> body = new HashMap<>();

        body.put("model", "llama3");
        body.put("prompt", prompt);
        body.put("stream", false);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<Map<String, Object>> request =
                new HttpEntity<>(body, headers);

        String response = restTemplate.postForObject(
                "http://localhost:11434/api/generate",
                request,
                String.class
        );

        return response;
    }
}