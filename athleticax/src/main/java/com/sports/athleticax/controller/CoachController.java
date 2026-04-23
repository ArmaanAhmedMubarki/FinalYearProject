package com.sports.athleticax.controller;

import com.sports.athleticax.services.CoachService;
import com.sports.athleticax.dto.CoachDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("api/coach")
public class CoachController {

    @Autowired
    private CoachService coachService;

    @GetMapping("/profile")
    public ResponseEntity<?> getCoachByEmail(@RequestParam String email) {
        try {
            CoachDTO coachProfile = coachService.getCoachByEmail(email);
            return ResponseEntity.ok(coachProfile);
        } catch (RuntimeException e) {
            // Return more specific error message
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User with email " + email + " not found.");
        }
    }
    @GetMapping("/search")
    public ResponseEntity<?> getCoachById(@RequestParam Long id) {
        try {
            CoachDTO coachProfile = coachService.getCoachById(id);
            return ResponseEntity.ok(coachProfile);
        } catch (RuntimeException e) {
            // Return more specific error message
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found.");
        }
    }

    @GetMapping
    public ResponseEntity<?> getAllCoach() {
        try {
            List<CoachDTO> coachProfile = coachService.getAllCoach();
            return ResponseEntity.ok(coachProfile);
        } catch (RuntimeException e) {
            // Return more specific error message
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
        }
    }

    @PutMapping("/profile")
    public ResponseEntity<?> updateCoachByEmail(@RequestParam String email, @RequestParam("CoachDTO") String coachDTO, // Receive as String and convert to CoachDTO
                                                @RequestParam(value = "imageLink", required = false) MultipartFile imageLink){
        // Convert JSON string to CoachDTO
        ObjectMapper objectMapper = new ObjectMapper();
        CoachDTO coachDTOObject;
        try {
            coachDTOObject = objectMapper.readValue(coachDTO, CoachDTO.class);
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
        coachService.updateCoachByEmail(email, coachDTOObject, imageBytes);

        return ResponseEntity.ok("Coach profile updated successfully!");
    }
}

