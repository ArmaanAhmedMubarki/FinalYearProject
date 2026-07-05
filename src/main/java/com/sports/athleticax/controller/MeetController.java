package com.sports.athleticax.controller;

import com.sports.athleticax.services.MeetService;
import com.sports.athleticax.dto.MeetDTO;
import com.sports.athleticax.entity.Meet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "http://localhost:63342")
@RestController
@RequestMapping("/api/meets")
public class MeetController
{

    @Autowired
    private MeetService meetService;

    @GetMapping
    public ResponseEntity<?> getMeet()
    {
        try
        {
            List<Meet> meetDetails = meetService.getMeet();
            return ResponseEntity.ok(meetDetails);
        }
        catch (RuntimeException e)
        {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Currently no meets going on");
        }
    }

    @GetMapping("/with-status")
    public ResponseEntity<?> getMeetsWithStatus()
    {
        try
        {
            List<MeetDTO> meetDTOs = meetService.getMeetDTOs();
            return ResponseEntity.ok(meetDTOs);
        }
        catch (RuntimeException e)
        {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Currently no meets available");
        }
    }

    @PostMapping("/set")
    public ResponseEntity<String> createMeet(@RequestBody Meet meet)
    {
        try
        {
            meetService.setMeet(meet);
            return ResponseEntity.ok("Meet created successfully");
        }
        catch (Exception e)
        {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error: " + e.getMessage());
        }
    }

    @PutMapping("/update")
    public ResponseEntity<String> updateMeet(@RequestBody Meet meet)
    {
        try
        {
            meetService.updateMeet(meet);
            return ResponseEntity.ok("Meet updated successfully");
        }
        catch (Exception e)
        {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error: " + e.getMessage());
        }
    }

    @DeleteMapping("/delete")
    public ResponseEntity<String> deleteMeet(@RequestParam Long id)
    {
        try
        {
            meetService.deleteMeet(id);
            return ResponseEntity.ok("Meet deleted successfully");
        }
        catch (Exception e)
        {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error: " + e.getMessage());
        }
    }
}

