package com.sports.athleticax.controller;

import com.sports.athleticax.services.NewsService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
@RestController
@RequestMapping("/api/news")
@CrossOrigin(origins = "https://resilient-centaur-8cbadb.netlify.app")
public class NewsController {

    @Autowired
    private NewsService newsService;

    @GetMapping("/sports")
    public ResponseEntity<String> sportsNews() {

        return ResponseEntity.ok(newsService.getSportsNews());

    }
}
