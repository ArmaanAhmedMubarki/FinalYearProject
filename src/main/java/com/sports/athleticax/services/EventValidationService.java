package com.sports.athleticax.services;

import com.sports.athleticax.entity.Event;
import com.sports.athleticax.entity.Meet;
import com.sports.athleticax.exception.InvalidEventDateException;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public class EventValidationService
{

    public void validateEventDate(Event event, Meet meet)
    {
        if (event == null || event.getEventDate() == null)
        {
            throw new InvalidEventDateException("Event and event date cannot be null");
        }

        if (meet == null || meet.getStartDate() == null || meet.getEndDate() == null)
        {
            throw new InvalidEventDateException("Meet must have valid start and end dates");
        }

        LocalDate eventDate = event.getEventDate();
        LocalDate meetStart = meet.getStartDate();
        LocalDate meetEnd = meet.getEndDate();
        LocalDate today = LocalDate.now();

        if (eventDate.isBefore(today))
        {
            throw new InvalidEventDateException(
                    "Event date (" + eventDate + ") cannot be in the past. Please select today or a future date."
            );
        }

        if (meetStart.isBefore(today))
        {
            throw new InvalidEventDateException(
                    "Meet start date (" + meetStart + ") cannot be in the past. Please create meets with today or future dates."
            );
        }

        if (eventDate.isBefore(meetStart))
        {
            throw new InvalidEventDateException(
                    "Event date (" + eventDate + ") cannot be before meet start date (" + meetStart + ")"
            );
        }

        if (eventDate.isAfter(meetEnd))
        {
            throw new InvalidEventDateException(
                    "Event date (" + eventDate + ") cannot be after meet end date (" + meetEnd + ")"
            );
        }
    }
}
