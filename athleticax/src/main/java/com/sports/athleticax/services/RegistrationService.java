package com.sports.athleticax.services;

import com.sports.athleticax.dto.RegistrationDTO;
import com.sports.athleticax.entity.Registration;
import com.sports.athleticax.repository.RegistrationRepository;
import com.sports.athleticax.repository.AthleteRepository;
import com.sports.athleticax.repository.EventRepository;

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

    // ===================== NORMAL FLOW ONLY =====================

    public List<Registration> getAllRegistrations() {
        return registrationRepository.findAll();
    }

    public void createRegistration(RegistrationDTO dto) {

        athleteRepository.findById(dto.getAthleteId())
                .orElseThrow(() -> new RuntimeException("Athlete not found"));

        eventRepository.findById(dto.getEventId())
                .orElseThrow(() -> new RuntimeException("Event not found"));

        Optional<Registration> existing =
                registrationRepository.findByEventIdAndAthleteId(
                        dto.getEventId(), dto.getAthleteId());

        if (existing.isPresent()) {
            throw new RuntimeException("Athlete already registered for this event");
        }

        Registration registration = new Registration();
        registration.setAthleteId(dto.getAthleteId());
        registration.setEventId(dto.getEventId());
        registration.setRegistrationDate(dto.getRegistrationDate());
        registration.setStatus("CONFIRMED");

        registrationRepository.save(registration);
    }

    public void updateRegistration(Long id, String status) {

        Registration registration = registrationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Registration not found"));

        registration.setStatus(status);
        registrationRepository.save(registration);
    }

    public void deleteRegistration(Long id) {

        Registration registration = registrationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Registration not found"));

        registrationRepository.delete(registration);
    }

    public Optional<Registration> findByEventIdAndAthleteId(Long eventId, Long athleteId) {
        return registrationRepository.findByEventIdAndAthleteId(eventId, athleteId);
    }

    public List<Registration> findByAthleteId(Long athleteId) {
        return registrationRepository.findByAthleteId(athleteId);
    }

    public List<Registration> findByEventId(Long eventId) {
        return registrationRepository.findByEventId(eventId);
    }
}
