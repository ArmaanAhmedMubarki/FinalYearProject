package com.sports.athleticax.controller;

import com.sports.athleticax.services.RegistrationService;
import com.sports.athleticax.dto.RegistrationDTO;
import com.sports.athleticax.entity.Registration;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.sports.athleticax.dto.EligibilityResultDTO;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Map;

@RestController
@RequestMapping("/api/registration")
public class RegistrationController {

    @Autowired
    private RegistrationService registrationService;

    // GET: Fetch all registrations
    @GetMapping("/all")
    public ResponseEntity<?> getAllRegistrations() {
        try {
            List<Registration> registrations = registrationService.getAllRegistrations();
            return ResponseEntity.ok(registrations);
        } catch (RuntimeException e) {
            // Return more specific error message
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No events available currently");
        }
    }

    // POST: Request a new registration
    @PostMapping("/create")
    public ResponseEntity<?> createRegistration (@RequestParam("registrationDTO") String registrationDTO){

        // Convert JSON string to RegistrationDTO
        ObjectMapper objectMapper = new ObjectMapper();
        RegistrationDTO registrationDTOObject;
        try {
            registrationDTOObject = objectMapper.readValue(registrationDTO, RegistrationDTO.class);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error parsing registrationDTO: " + e.getMessage());
        }

        // Pass the DTO and image bytes to the service for updating the profile
        registrationService.createRegistration(registrationDTOObject);

        return ResponseEntity.ok("Registration request Successful!");
    }


    // PUT: Update registration status
    @PutMapping("/update")
    public ResponseEntity<?> updateRegistration(@RequestParam Long id, @RequestParam String status) {

        // Pass the DTO to the service for updating the registrations
        registrationService.updateRegistration(id, status);

        return ResponseEntity.ok("Event update successful");

    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteRegistration(@PathVariable Long id){
        try {
            registrationService.deleteRegistration(id);
            return ResponseEntity.ok("Registration cancelled successfully");
        }catch (RuntimeException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No request to delete");
        }
    }

    @GetMapping("/search")
    public ResponseEntity<?> getRegistration(
            @RequestParam(required = false) Long eventId,
            @RequestParam(required = false) Long athleteId)
    {
        if (eventId != null && athleteId != null)
        {
            Optional<Registration> registration = registrationService.findByEventIdAndAthleteId(eventId, athleteId);
            return registration.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
        }
        else if (eventId != null)
        {
            List<Registration> registrations = registrationService.findByEventId(eventId);
            return ResponseEntity.ok(registrations);
        }
        else if (athleteId != null)
        {
            List<Registration> registrations = registrationService.findByAthleteId(athleteId);
            return ResponseEntity.ok(registrations);
        }
        else
        {
            return ResponseEntity.badRequest().body("Either eventId or athleteId must be provided.");
        }
    }

    @GetMapping("/eligible-candidates")
    public ResponseEntity<?> getEligibleCandidates(@RequestParam Long eventId)
    {
        try
        {
            List<EligibilityResultDTO> candidates = registrationService.getEligibleCandidates(eventId);
            return ResponseEntity.ok(candidates);
        }
        catch (RuntimeException e)
        {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Error: " + e.getMessage());
        }
    }

    @PutMapping("/bulk-update")
    public ResponseEntity<?> bulkUpdateRegistration(@RequestBody Map<String, Object> request)
    {
        try
        {
            List<Long> registrationIds = (List<Long>) request.get("registrationIds");
            String status = (String) request.get("status");

            if (registrationIds == null || registrationIds.isEmpty())
            {
                return ResponseEntity.badRequest().body("No registrations selected");
            }

            if (status == null || status.isEmpty())
            {
                return ResponseEntity.badRequest().body("Status is required");
            }

            for (Long id : registrationIds)
            {
                registrationService.updateRegistration(id, status);
            }

            return ResponseEntity.ok("Bulk update successful for " + registrationIds.size() + " registrations");
        }
        catch (Exception e)
        {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error: " + e.getMessage());
        }
    }
}



