package com.sports.athleticax.controller;

import com.sports.athleticax.dto.EventDTO;
import com.sports.athleticax.entity.Event;
import com.sports.athleticax.exception.InvalidEventDateException;
import com.sports.athleticax.services.EventService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@CrossOrigin(origins = "http://localhost:63342")
@RestController
@RequestMapping("/api/events")
public class EventController
{

    @Autowired
    private EventService eventService;

    @GetMapping
    public ResponseEntity<?> getAllEvents()
    {
        try
        {
            List<Event> events = eventService.getAllEvents();
            return ResponseEntity.ok(events);
        }
        catch (RuntimeException e)
        {
            e.printStackTrace(); 
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No events available currently");
        }
    }

    @PostMapping("/create")
    public ResponseEntity<?> createEvent(
            @RequestParam("eventDTO") String eventDTO,
            @RequestParam(value = "imageLink") MultipartFile imageLink)
    {
        ObjectMapper objectMapper = new ObjectMapper();
        EventDTO eventDTOObject;

        try
        {
            eventDTOObject = objectMapper.readValue(eventDTO, EventDTO.class);
        }
        catch (Exception e)
        {
            return ResponseEntity.badRequest().body("Error parsing eventDTO: " + e.getMessage());
        }

        byte[] imageBytes = null;

        if (imageLink != null && !imageLink.isEmpty())
        {
            try
            {
                imageBytes = imageLink.getBytes();
            }
            catch (Exception e)
            {
                return ResponseEntity.badRequest().body("Error processing image: " + e.getMessage());
            }
        }

        try
        {
            eventService.createEvent(eventDTOObject, imageBytes);
            return ResponseEntity.ok("Event Creation Successful!");
        }
        catch (InvalidEventDateException e)
        {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Event Date Validation Error: " + e.getMessage());
        }
        catch (Exception e)
        {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error creating event: " + e.getMessage());
        }
    }

    @DeleteMapping("/delete")
    public ResponseEntity<?> deleteEvent(@RequestParam Long id)
    {
        try
        {
            eventService.deleteEvent(id);
            return ResponseEntity.ok("Event deletion successful");
        }
        catch (RuntimeException e)
        {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No event to delete");
        }
    }

    @PutMapping("/update")
    public ResponseEntity<?> updateEvent(
            @RequestParam("eventDTO") String eventDTO,
            @RequestParam(value = "imageLink", required = false) MultipartFile imageLink)
    {
        ObjectMapper objectMapper = new ObjectMapper();
        EventDTO eventDTOObject;

        try
        {
            eventDTOObject = objectMapper.readValue(eventDTO, EventDTO.class);
        }
        catch (Exception e)
        {
            return ResponseEntity.badRequest().body("Error parsing eventDTO: " + e.getMessage());
        }

        byte[] imageBytes = null;

        if (imageLink != null && !imageLink.isEmpty())
        {
            try
            {
                imageBytes = imageLink.getBytes();
            }
            catch (Exception e)
            {
                return ResponseEntity.badRequest().body("Error processing image: " + e.getMessage());
            }
        }

        try
        {
            eventService.updateEvent(eventDTOObject, imageBytes);
            return ResponseEntity.ok("Event update successful");
        }
        catch (InvalidEventDateException e)
        {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Event Date Validation Error: " + e.getMessage());
        }
        catch (Exception e)
        {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error updating event: " + e.getMessage());
        }
    }

    @GetMapping("/search")
    public ResponseEntity<?> getEventById(@RequestParam Long id)
    {
        try
        {
            Event event = eventService.getEventById(id);
            if (event == null)
            {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Event not found");
            }
            return ResponseEntity.ok(event);
        }
        catch (RuntimeException e)
        {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Error retrieving event: " + e.getMessage());
        }
    }
}