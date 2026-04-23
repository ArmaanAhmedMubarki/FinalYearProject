package com.sports.athleticax.dto;

public class RegistrationDTO {

    private Long id;
    private Long athleteId;
    private Long eventId;
    private String registrationDate; // Stored as String
    private String status;

    // Constructors
    public RegistrationDTO() {}

    public RegistrationDTO(Long id, Long athleteId, Long eventId, String registrationDate, String status) {
        this.id = id;
        this.athleteId = athleteId;
        this.eventId = eventId;
        this.registrationDate = registrationDate;
        this.status = status;
    }

    public Long getAthleteId() {
        return athleteId;
    }

    public void setAthleteId(Long athleteId) {
        this.athleteId = athleteId;
    }

    public Long getEventId() {
        return eventId;
    }

    public void setEventId(Long eventId) {
        this.eventId = eventId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getRegistrationDate() {
        return registrationDate;
    }

    public void setRegistrationDate(String registrationDate) {
        this.registrationDate = registrationDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}

