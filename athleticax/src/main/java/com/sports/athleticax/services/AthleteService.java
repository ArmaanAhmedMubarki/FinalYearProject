package com.sports.athleticax.services;

import com.sports.athleticax.repository.AthleteRepository;
import com.sports.athleticax.repository.UserRepository;
import com.sports.athleticax.dto.AthleteDTO;
import com.sports.athleticax.dto.CoachDTO;
import com.sports.athleticax.entity.Admin;
import com.sports.athleticax.entity.Athlete;
import com.sports.athleticax.entity.Coach;
import com.sports.athleticax.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Base64;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class AthleteService {

    @Autowired
    private AthleteRepository athleteRepository;

    @Autowired
    private UserRepository userRepository;

    public AthleteDTO getAthleteByEmail(String email) {

        // Find the user by email
        User user = userRepository.findByEmail(email);

        if (user == null) {
            throw new RuntimeException("User not found");
        }


        // Find the associated Athlete profile by userId
        Athlete athlete = athleteRepository.findByUserId(user.getId())
                .orElseThrow(() -> new RuntimeException("Athlete profile not found"));

        // Return AthleteDTO with user and athlete data
        AthleteDTO athleteDTO = new AthleteDTO(
                user.getName(),
                athlete.getBirthDate(),
                athlete.getGender(),
                athlete.getId(),
                athlete.getWeight(),
                athlete.getImageLink(),
                athlete.getHeight(),
                athlete.getCategory(),
                user.getEmail()
        );

        return athleteDTO;
    }

    public void updateAthleteByEmail(String email, AthleteDTO athleteDTOObject, byte[] imageBytes) {

        User user = userRepository.findByEmail(email);

        if (user == null) {
            throw new RuntimeException("User not found");
        }
        user.setName(athleteDTOObject.getName());

        // Find the associated Admin profile by userId
        Athlete athlete = athleteRepository.findByUserId(user.getId())
                .orElseThrow(() -> new RuntimeException("Athlete profile not found"));
        athlete.setBirthDate(athleteDTOObject.getBirthDate());
        athlete.setGender(athleteDTOObject.getGender());
        athlete.setWeight(athleteDTOObject.getWeight());
        athlete.setHeight(athleteDTOObject.getHeight());
        athlete.setCategory(athleteDTOObject.getCategory());

        // Convert byte[] to Base64 string and set it in the Athlete entity
        if (imageBytes != null && imageBytes.length > 0) {
            String base64Image = Base64.getEncoder().encodeToString(imageBytes);
            athlete.setImageLink(base64Image);
        }

        userRepository.save(user);
        athleteRepository.save(athlete);
    }

    public AthleteDTO getAthleteById(Long id) {

        // Find the associated Athlete profile by id
        Athlete athlete = athleteRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Athlete profile not found"));

        User user = athlete.getUser();

        // Return AthleteDTO with user and athlete data
        AthleteDTO athleteDTO = new AthleteDTO(
                user.getName(),
                athlete.getBirthDate(),
                athlete.getGender(),
                athlete.getId(),
                athlete.getWeight(),
                athlete.getImageLink(),
                athlete.getHeight(),
                athlete.getCategory(),
                user.getEmail()
        );

        return athleteDTO;
    }

    public List<AthleteDTO> getAllAthlete() {
        List<Athlete> athletes = athleteRepository.findAll();

        // Transform entities into DTOs
        return athletes.stream()
                .map(athlete -> new AthleteDTO(
                        athlete.getUser().getName(),
                        athlete.getBirthDate(),
                        athlete.getGender(),
                        athlete.getId(),
                        athlete.getWeight(),
                        athlete.getImageLink(),
                        athlete.getHeight(),
                        athlete.getCategory(),
                        athlete.getUser().getEmail()
                ))
                .collect(Collectors.toList());
    }
}
