package com.sports.athleticax.dto;

import com.fasterxml.jackson.annotation.JsonFormat;

public class EventDTO
{
    private Long id;
    private String category;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy")
    private String eventDate;
    private String eventName;
    private String imageLink;
    private String meetName;
    private String location;
    private Integer minAge;
    private Integer maxAge;
    private String genderRestriction;
    private Long minWeight;
    private Long maxWeight;
    private Long minHeight;
    private Long maxHeight;

    public EventDTO(String category, String meetName, String imageLink, Long id, String eventName, String eventDate,String location) {
        this.category = category;
        this.meetName = meetName;
        this.imageLink = imageLink;
        this.id = id;
        this.eventName = eventName;
        this.eventDate = eventDate;
        this.location = location;
    }

    // Default constructor
    public EventDTO() {
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getEventDate() {
        return eventDate;
    }

    public void setEventDate(String eventDate) {
        this.eventDate = eventDate;
    }

    public String getEventName() {
        return eventName;
    }

    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getImageLink() {
        return imageLink;
    }

    public void setImageLink(String imageLink) {
        this.imageLink = imageLink;
    }

    public String getMeetName() {
        return meetName;
    }

    public void setMeetName(String meetName)
    {
        this.meetName = meetName;
    }

    public Integer getMinAge()
    {
        return minAge;
    }

    public void setMinAge(Integer minAge)
    {
        this.minAge = minAge;
    }

    public Integer getMaxAge()
    {
        return maxAge;
    }

    public void setMaxAge(Integer maxAge)
    {
        this.maxAge = maxAge;
    }

    public String getGenderRestriction()
    {
        return genderRestriction;
    }

    public void setGenderRestriction(String genderRestriction)
    {
        this.genderRestriction = genderRestriction;
    }

    public Long getMinWeight()
    {
        return minWeight;
    }

    public void setMinWeight(Long minWeight)
    {
        this.minWeight = minWeight;
    }

    public Long getMaxWeight()
    {
        return maxWeight;
    }

    public void setMaxWeight(Long maxWeight)
    {
        this.maxWeight = maxWeight;
    }

    public Long getMinHeight()
    {
        return minHeight;
    }

    public void setMinHeight(Long minHeight)
    {
        this.minHeight = minHeight;
    }

    public Long getMaxHeight()
    {
        return maxHeight;
    }

    public void setMaxHeight(Long maxHeight)
    {
        this.maxHeight = maxHeight;
    }
}