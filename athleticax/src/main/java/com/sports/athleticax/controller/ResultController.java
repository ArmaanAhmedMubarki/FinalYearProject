//package com.sports.athleticax.controller;
//
//import com.sports.athleticax.services.ResultService;
//import com.sports.athleticax.dto.AssistanceDTO;
//import com.sports.athleticax.dto.ResultDTO;
//import com.sports.athleticax.entity.Assistance;
//import com.sports.athleticax.entity.Registration;
//import com.sports.athleticax.entity.Result;
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
//@RequestMapping("/api/result")
//public class ResultController {
//
//    @Autowired
//    private ResultService resultService;
//
//    @GetMapping("/search")
//    public ResponseEntity<?> getResult(
//            @RequestParam(required = false) Long athleteId,
//            @RequestParam(required = false) Long eventId) {
//
//        if (athleteId != null && eventId != null) {
//            // Search by both eventId and athleteId
//            Optional<Result> result = resultService.findByAthleteIDAndEventId(athleteId, eventId);
//            return result.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
//        } else if (athleteId != null) {
//            // Search by athleteId only
//            List<Result> result = resultService.findByAthleteId(athleteId);
//            return ResponseEntity.ok(result);
//        } else if (eventId != null) {
//            // Search by eventId only
//            List<Result> result = resultService.findByEventId(eventId);
//            return ResponseEntity.ok(result);
//        } else {
//            return ResponseEntity.badRequest().body("Either athleteId or eventId must be provided.");
//        }
//    }
//
//    // POST: publish results
//    @PostMapping("/publish")
//    public ResponseEntity<?> createResult(@RequestParam("resultDTO") String resultDTO){
//
//        // Convert JSON string to ResultDTO
//        ObjectMapper objectMapper = new ObjectMapper();
//        ResultDTO resultDTOObject;
//        try {
//            resultDTOObject = objectMapper.readValue(resultDTO, ResultDTO.class);
//        } catch (Exception e) {
//            return ResponseEntity.badRequest().body("Error parsing resultDTO: " + e.getMessage());
//        }
//
//        resultService.createResult(resultDTOObject);
//
//        return ResponseEntity.ok("Result publishing Successful!");
//    }
//
//    // GET: Fetch all registrations
//    @GetMapping("/all")
//    public ResponseEntity<?> getAllResult() {
//        try {
//            List<Result> result = resultService.getAllResult();
//            return ResponseEntity.ok(result);
//        } catch (RuntimeException e) {
//            // Return more specific error message
//            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No results available currently");
//        }
//    }
//}
//
