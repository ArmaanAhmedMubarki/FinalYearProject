package com.sports.athleticax.services;

import com.sports.athleticax.repository.MeetRepository;
import com.sports.athleticax.dto.MeetDTO;
import com.sports.athleticax.entity.Meet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
public class MeetService
{

    @Autowired
    private MeetRepository meetRepository;

    public List<Meet> getMeet()
    {
        List<Meet> meetDetails = meetRepository.findAll();

        if (meetDetails.isEmpty())
        {
            throw new RuntimeException("No meet available");
        }

        return meetDetails;
    }

    public List<MeetDTO> getMeetDTOs()
    {
        List<Meet> meets = meetRepository.findAll();
        List<MeetDTO> meetDTOs = new ArrayList<>();

        for (Meet meet : meets)
        {
            MeetDTO dto = new MeetDTO(
                    meet.getId(),
                    meet.getName(),
                    meet.getStartDate(),
                    meet.getEndDate(),
                    meet.getStatus()
            );
            meetDTOs.add(dto);
        }

        return meetDTOs;
    }

    public void setMeet(Meet meet)
    {
        if (meet.getName() == null || meet.getName().isEmpty())
        {
            throw new RuntimeException("A meet name is required");
        }

        if (meet.getStartDate() == null)
        {
            throw new RuntimeException("A meet start date is required");
        }

        if (meet.getEndDate() == null)
        {
            throw new RuntimeException("A meet end date is required");
        }

        LocalDate today = LocalDate.now();
        if (meet.getStartDate().isBefore(today))
        {
            throw new RuntimeException("Meet start date cannot be in the past. Please select today or a future date.");
        }

        if (meet.getEndDate().isBefore(today))
        {
            throw new RuntimeException("Meet end date cannot be in the past. Please select today or a future date.");
        }

        if (meet.getStartDate().isAfter(meet.getEndDate()))
        {
            throw new RuntimeException("Start date cannot be after end date");
        }

        meetRepository.save(meet);
    }

    public void updateMeet(Meet meet)
    {
        if (meet.getId() == 0)
        {
            throw new RuntimeException("Meet ID is required for update");
        }

        if (meet.getName() == null || meet.getName().isEmpty())
        {
            throw new RuntimeException("A meet name is required");
        }

        if (meet.getStartDate() == null)
        {
            throw new RuntimeException("A meet start date is required");
        }

        if (meet.getEndDate() == null)
        {
            throw new RuntimeException("A meet end date is required");
        }

        LocalDate today = LocalDate.now();
        if (meet.getStartDate().isBefore(today))
        {
            throw new RuntimeException("Meet start date cannot be in the past. Please select today or a future date.");
        }

        if (meet.getEndDate().isBefore(today))
        {
            throw new RuntimeException("Meet end date cannot be in the past. Please select today or a future date.");
        }

        if (meet.getStartDate().isAfter(meet.getEndDate()))
        {
            throw new RuntimeException("Start date cannot be after end date");
        }

        meetRepository.save(meet);
    }

    public void deleteMeet(Long id)
    {
        meetRepository.delete(meetRepository.getReferenceById(id));
    }
}


