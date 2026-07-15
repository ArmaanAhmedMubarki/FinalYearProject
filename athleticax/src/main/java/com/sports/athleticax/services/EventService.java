package com.sports.athleticax.services;

import com.sports.athleticax.dto.EventDTO;
import com.sports.athleticax.entity.Event;
import com.sports.athleticax.entity.Meet;
// import com.sports.athleticax.exception.InvalidEventDateException;
import com.sports.athleticax.repository.EventRepository;
import com.sports.athleticax.repository.MeetRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Base64;
import java.util.List;

@Service
public class EventService
{

    @Autowired
    private EventRepository eventRepository;

    @Autowired
    private MeetRepository meetRepository;

    @Autowired
    private EventValidationService eventValidationService;


    public List<Event> getAllEvents()
    {
        return eventRepository.findAll();
    }

    public Event getEventById(Long id)
    {
        return eventRepository.findById(id).orElse(null);
    }

    public void createEvent(EventDTO eventDTOObject, byte[] imageBytes)
    {
        Event event = new Event();
        event.setEventName(eventDTOObject.getEventName());

        LocalDate eventDate = LocalDate.parse(eventDTOObject.getEventDate());
        event.setEventDate(eventDate);

        event.setCategory(eventDTOObject.getCategory());

        Meet meet;
        try
        {
            meet = meetRepository.findByName(eventDTOObject.getMeetName());
        }
        catch (Exception e)
        {
            throw new RuntimeException("Duplicate meet names found in database. Please contact administrator to resolve this issue.");
        }

        if (meet == null)
        {
            throw new RuntimeException("Meet not found with name: " + eventDTOObject.getMeetName());
        }

        event.setMeet(meet);

        eventValidationService.validateEventDate(event, meet);

        event.setLocation(eventDTOObject.getLocation());

        event.setMinAge(eventDTOObject.getMinAge());
        event.setMaxAge(eventDTOObject.getMaxAge());
        event.setGenderRestriction(eventDTOObject.getGenderRestriction());
        event.setMinWeight(eventDTOObject.getMinWeight());
        event.setMaxWeight(eventDTOObject.getMaxWeight());
        event.setMinHeight(eventDTOObject.getMinHeight());
        event.setMaxHeight(eventDTOObject.getMaxHeight());

        if (imageBytes != null && imageBytes.length > 0)
        {
            String base64Image = Base64.getEncoder().encodeToString(imageBytes);
            event.setImageLink(base64Image);
        }

        eventRepository.save(event);
    }

    public void deleteEvent(Long id)
    {
        eventRepository.delete(eventRepository.getReferenceById(id));
    }

    public void updateEvent(EventDTO eventDTOObject, byte[] imageBytes)
    {
        Event event = eventRepository.getReferenceById(eventDTOObject.getId());
        event.setEventName(eventDTOObject.getEventName());

        LocalDate eventDate = LocalDate.parse(eventDTOObject.getEventDate());
        event.setEventDate(eventDate);

        event.setCategory(eventDTOObject.getCategory());

        Meet meet;
        try
        {
            meet = meetRepository.findByName(eventDTOObject.getMeetName());
        }
        catch (Exception e)
        {
            throw new RuntimeException("Duplicate meet names found in database. Please contact administrator to resolve this issue.");
        }

        if (meet == null)
        {
            throw new RuntimeException("Meet not found with name: " + eventDTOObject.getMeetName());
        }

        event.setMeet(meet);

        eventValidationService.validateEventDate(event, meet);

        event.setLocation(eventDTOObject.getLocation());

        event.setMinAge(eventDTOObject.getMinAge());
        event.setMaxAge(eventDTOObject.getMaxAge());
        event.setGenderRestriction(eventDTOObject.getGenderRestriction());
        event.setMinWeight(eventDTOObject.getMinWeight());
        event.setMaxWeight(eventDTOObject.getMaxWeight());
        event.setMinHeight(eventDTOObject.getMinHeight());
        event.setMaxHeight(eventDTOObject.getMaxHeight());

        if (imageBytes != null && imageBytes.length > 0)
        {
            String base64Image = Base64.getEncoder().encodeToString(imageBytes);
            event.setImageLink(base64Image);
        }

        eventRepository.save(event);
    }

}