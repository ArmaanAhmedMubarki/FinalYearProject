//package com.sports.athleticax.entity;
//
//import jakarta.persistence.*;
//
//@Entity
//@Table(name = "assistance")
//public class Assistance {
//
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private Long id;
//
//    private Long athleteId;
//
//    private Long coachId;
//
//    private String requestDate; // Stored as String to match the database schema
//
//    private String status;
//
//    private String remarks;
//
//    public Long getAthleteId() {
//        return athleteId;
//    }
//
//    public void setAthleteId(Long athleteId) {
//        this.athleteId = athleteId;
//    }
//
//    public Long getCoachId() {
//        return coachId;
//    }
//
//    public void setCoachId(Long coachId) {
//        this.coachId = coachId;
//    }
//
//    public Long getId() {
//        return id;
//    }
//
//    public void setId(Long id) {
//        this.id = id;
//    }
//
//    public String getRemarks() {
//        return remarks;
//    }
//
//    public void setRemarks(String remarks) {
//        this.remarks = remarks;
//    }
//
//    public String getRequestDate() {
//        return requestDate;
//    }
//
//    public void setRequestDate(String requestDate) {
//        this.requestDate = requestDate;
//    }
//
//    public String getStatus() {
//        return status;
//    }
//
//    public void setStatus(String status) {
//        this.status = status;
//    }
//}
//
