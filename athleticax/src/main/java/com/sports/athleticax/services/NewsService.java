package com.sports.athleticax.services;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
@Service
public class NewsService {

    @Value("${news.api.key}")
    private String apiKey;

    private final RestTemplate restTemplate = new RestTemplate();

    public String getSportsNews() {

        String url =
                "https://newsapi.org/v2/top-headlines?category=sports&apiKey=" + apiKey;

        return restTemplate.getForObject(url, String.class);

    }
}
