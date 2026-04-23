package com.sports.athleticax.services;

import com.sports.athleticax.repository.AthleteRepository;
import com.sports.athleticax.repository.EventRepository;
import com.sports.athleticax.repository.RegistrationRepository;
import com.sports.athleticax.dto.RegistrationDTO;
import com.sports.athleticax.entity.Registration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class RegistrationService {

    @Autowired
    private RegistrationRepository registrationRepository;

    @Autowired
    private AthleteRepository athleteRepository;

    @Autowired
    private EventRepository eventRepository;

    //get All registrations
    public List<Registration> getAllRegistrations() {
        return registrationRepository.findAll();
    }
    public void createRegistration (RegistrationDTO registrationDTOObject){

        Registration registration = new Registration();
        registration.setAthleteId(registrationDTOObject.getAthleteId());
        registration.setEventId(registrationDTOObject.getEventId());
        registration.setRegistrationDate(registrationDTOObject.getRegistrationDate());
        registration.setStatus("PENDING");

        registrationRepository.save(registration);
    }


    public void updateRegistration(Long id, String status) {

        Registration registration = registrationRepository.getReferenceById(id);
        registration.setStatus(status);
        registrationRepository.save(registration);
    }

    public void deleteRegistration(Long id) {
        Registration registration = registrationRepository.getReferenceById(id);
        registrationRepository.delete(registration);
    }

    // Find registration by eventId and athleteId
    public Optional<Registration> findByEventIdAndAthleteId(Long eventId, Long athleteId) {
        return registrationRepository.findByEventIdAndAthleteId(eventId, athleteId);
    }

    // Find all registrations for a specific athlete
    public List<Registration> findByAthleteId(Long athleteId) {
        return registrationRepository.findByAthleteId(athleteId);
    }

    // Find all registrations for a specific event
    public List<Registration> findByEventId(Long eventId) {
        return registrationRepository.findByEventId(eventId);
    }
}
