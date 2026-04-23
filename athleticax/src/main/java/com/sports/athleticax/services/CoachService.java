package com.sports.athleticax.services;

import com.sports.athleticax.repository.CoachRepository;
import com.sports.athleticax.repository.UserRepository;
import com.sports.athleticax.dto.AthleteDTO;
import com.sports.athleticax.dto.CoachDTO;
import com.sports.athleticax.entity.Athlete;
import com.sports.athleticax.entity.Coach;
import com.sports.athleticax.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Base64;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CoachService {

    @Autowired
    private CoachRepository coachRepository;

    @Autowired
    private UserRepository userRepository;

    public CoachDTO getCoachByEmail(String email) {
        // Find the user by email
        User user = userRepository.findByEmail(email);

        if (user == null) {
            throw new RuntimeException("User not found");
        }


        // Find the associated Coach profile by userId
        Coach coach = coachRepository.findByUserId(user.getId())
                .orElseThrow(() -> new RuntimeException("Athlete profile not found"));

        // Return CoachDTO with user and coach data
        CoachDTO coachDTO = new CoachDTO(
                coach.getId(),
                coach.getBirthDate(),
                coach.getGender(),
                coach.getCategory(),
                coach.getImageLink(),
                user.getName()
        );

        return coachDTO;
    }

    public void updateCoachByEmail(String email, CoachDTO coachDTOObject, byte[] imageBytes) {

        User user = userRepository.findByEmail(email);

        if (user == null) {
            throw new RuntimeException("User not found");
        }
        user.setName(coachDTOObject.getName());

        // Find the associated Coach profile by userId
        Coach coach = coachRepository.findByUserId(user.getId())
                .orElseThrow(() -> new RuntimeException("Coach profile not found"));
        coach.setBirthDate(coachDTOObject.getBirthDate());
        coach.setGender(coachDTOObject.getGender());
        coach.setCategory(coachDTOObject.getCategory());

        // Convert byte[] to Base64 string and set it in the Athlete entity
        if (imageBytes != null && imageBytes.length > 0) {
            String base64Image = Base64.getEncoder().encodeToString(imageBytes);
            coach.setImageLink(base64Image);
        }

        userRepository.save(user);
        coachRepository.save(coach);
    }

    public List<CoachDTO> getAllCoach() {

        List<Coach> coaches = coachRepository.findAll();

        // Transform entities into DTOs
        return coaches.stream()
                .map(coach -> new CoachDTO(
                        coach.getId(),
                        coach.getBirthDate(),
                        coach.getGender(),
                        coach.getCategory(),
                        coach.getImageLink(),
                        coach.getUser().getName() // Extract user's name
                ))
                .collect(Collectors.toList());
    }

    public CoachDTO getCoachById(Long id) {

        // Find the associated Athlete profile by id
        Coach coach = coachRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Athlete profile not found"));

        User user = coach.getUser();

        // Return AthleteDTO with user and athlete data
        CoachDTO coachDTO = new CoachDTO(
                coach.getId(),
                coach.getBirthDate(),
                coach.getGender(),
                coach.getCategory(),
                coach.getImageLink(),
                coach.getUser().getName()
        );
        return coachDTO;
    }
}

