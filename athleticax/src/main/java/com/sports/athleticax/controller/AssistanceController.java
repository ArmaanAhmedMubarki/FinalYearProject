//package com.sports.athleticax.controller;
//
//import com.sports.athleticax.services.AssistanceService;
//import com.sports.athleticax.dto.AssistanceDTO;
//import com.sports.athleticax.entity.Assistance;
//import com.fasterxml.jackson.databind.ObjectMapper;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.*;
//
//import java.util.List;
//import java.util.Optional;
//
//@RestController
//@RequestMapping("/api/assistance")
//public class AssistanceController {
//
//    @Autowired
//    private AssistanceService assistanceService;
//
//    @GetMapping("/search")
//    public ResponseEntity<?> getAssistance(
//            @RequestParam(required = false) Long coachId,
//            @RequestParam(required = false) Long athleteId) {
//
//        if (coachId != null && athleteId != null) {
//            // Search by both coachId and athleteId
//            Optional<Assistance> assistance = assistanceService.findByCoachIdAndAthleteId(coachId, athleteId);
//            return assistance.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
//        } else if (coachId != null) {
//            // Search by coachId only
//            List<Assistance> assistance = assistanceService.findByCoachId(coachId);
//            return ResponseEntity.ok(assistance);
//        } else if (athleteId != null) {
//            // Search by athleteId only
//            List<Assistance> assistance = assistanceService.findByAthleteId(athleteId);
//            return ResponseEntity.ok(assistance);
//        } else {
//            return ResponseEntity.badRequest().body("Either coachId or athleteId must be provided.");
//        }
//    }
//
//    // POST: Request a new assistance
//    @PostMapping("/request")
//    public ResponseEntity<?> createAssistance(@RequestParam("assistanceDTO") String assistanceDTO){
//
//        // Convert JSON string to AssistanceDTO
//        ObjectMapper objectMapper = new ObjectMapper();
//        AssistanceDTO assistanceDTOObject;
//        try {
//            assistanceDTOObject = objectMapper.readValue(assistanceDTO, AssistanceDTO.class);
//        } catch (Exception e) {
//            return ResponseEntity.badRequest().body("Error parsing assistanceDTO: " + e.getMessage());
//        }
//
//        assistanceService.createAssistance(assistanceDTOObject);
//
//        return ResponseEntity.ok("Assistance request Successful!");
//    }
//
//    // PUT: Update Assistance status
//    @PutMapping("/update")
//    public ResponseEntity<?> updateAssistance(@RequestParam Long id, @RequestParam String status, @RequestParam String remarks) {
//
//        // Pass the DTO to the service for updating the Assistance
//        assistanceService.updateAssistance(id, status, remarks);
//
//        return ResponseEntity.ok("Assistance update successful");
//
//    }
//
//    @DeleteMapping("/delete/{id}")
//    public ResponseEntity<?> deleteAssistance(@PathVariable Long id){
//        try {
//            assistanceService.deleteAssistance(id);
//            return ResponseEntity.ok("Assistance request cancelled successfully");
//        }catch (RuntimeException e){
//            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No request to delete");
//        }
//    }
//}
//
