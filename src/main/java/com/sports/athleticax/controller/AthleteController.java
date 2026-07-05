package com.sports.athleticax.controller;

import com.sports.athleticax.services.AthleteService;
import com.sports.athleticax.dto.AdminDTO;
import com.sports.athleticax.dto.AthleteDTO;
import com.sports.athleticax.dto.CoachDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/athlete")
public class AthleteController {

    @Autowired
    private AthleteService athleteService;

    @GetMapping("/profile")
    public ResponseEntity<?> getAthleteByEmail(@RequestParam String email) {
        try {
            AthleteDTO athleteProfile = athleteService.getAthleteByEmail(email);
            return ResponseEntity.ok(athleteProfile);
        } catch (RuntimeException e) {
            // Return more specific error message
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User with email " + email + " not found.");
        }
    }

    @PutMapping("/profile")
    public ResponseEntity<?> updateAthleteByEmail(@RequestParam String email, @RequestParam("AthleteDTO") String athleteDTO, // Receive as String and convert to AthleteDTO
                                                  @RequestParam(value = "imageLink", required = false) MultipartFile imageLink){
        // Convert JSON string to AthleteDTO
        ObjectMapper objectMapper = new ObjectMapper();
        AthleteDTO athleteDTOObject;
        try {
            athleteDTOObject = objectMapper.readValue(athleteDTO, AthleteDTO.class);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error parsing athleteDTO: " + e.getMessage());
        }

        byte[] imageBytes = null;

        // If a profile image is uploaded, convert it to byte[] for processing
        if (imageLink != null && !imageLink.isEmpty()) {
            try {
                imageBytes = imageLink.getBytes(); // Convert the image to byte[] format
            } catch (Exception e) {
                return ResponseEntity.badRequest().body("Error processing image: " + e.getMessage());
            }
        }

        // Pass the DTO and image bytes to the service for updating the profile
        athleteService.updateAthleteByEmail(email, athleteDTOObject, imageBytes);

        return ResponseEntity.ok("Athlete profile updated successfully!");
    }

    @GetMapping("/profile/search")
    public ResponseEntity<?> getAthleteByAthleteId(@RequestParam Long id) {
        try {
            AthleteDTO athleteProfile = athleteService.getAthleteById(id);
            return ResponseEntity.ok(athleteProfile);
        } catch (RuntimeException e) {
            // Return more specific error message
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("NO Athlete found");
        }
    }

    @GetMapping
    public ResponseEntity<?> getAllAthlete() {
        try {
            List<AthleteDTO> athleteProfile = athleteService.getAllAthlete();
            return ResponseEntity.ok(athleteProfile);
        } catch (RuntimeException e) {
            // Return more specific error message
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
        }
    }

}
