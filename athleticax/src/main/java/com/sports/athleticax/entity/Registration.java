package com.sports.athleticax.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "registrations")
public class Registration
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long athleteId;

    private Long eventId;

    private String registrationDate;

    private String status;

    @Enumerated(EnumType.STRING)
    @Column(nullable = true)
    private EligibilityStatus eligibilityStatus;

    @Column(nullable = true, length = 500)
    private String rejectionReason;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public String getRegistrationDate() {
        return registrationDate;
    }

    public void setRegistrationDate(String registrationDate) {
        this.registrationDate = registrationDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status)
    {
        this.status = status;
    }

    public EligibilityStatus getEligibilityStatus()
    {
        return eligibilityStatus;
    }

    public void setEligibilityStatus(EligibilityStatus eligibilityStatus)
    {
        this.eligibilityStatus = eligibilityStatus;
    }

    public String getRejectionReason()
    {
        return rejectionReason;
    }

    public void setRejectionReason(String rejectionReason)
    {
        this.rejectionReason = rejectionReason;
    }
}