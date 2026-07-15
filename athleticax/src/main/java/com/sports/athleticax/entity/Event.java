package com.sports.athleticax.entity;

import jakarta.persistence.*;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonInclude;
import java.time.LocalDate;


@Entity
@Table(name = "events")
public class Event
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String eventName;

    @Column(nullable = false)
    private String category;

    @Column(nullable = false)
    private LocalDate eventDate;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "meet_id", nullable = false)
    @JsonProperty("meet")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Meet meet;

    @Column(nullable = false)
    private String location;

    @Lob
    private String imageLink;

    @Column(nullable = true)
    private Integer minAge;

    @Column(nullable = true)
    private Integer maxAge;

    @Column(nullable = true)
    private String genderRestriction;

    @Column(nullable = true)
    private Long minWeight;

    @Column(nullable = true)
    private Long maxWeight;

    @Column(nullable = true)
    private Long minHeight;

    @Column(nullable = true)
    private Long maxHeight;


    public Long getId()
    {
        return id;
    }

    public void setId(Long id)
    {
        this.id = id;
    }

    public String getEventName()
    {
        return eventName;
    }

    public void setEventName(String eventName)
    {
        this.eventName = eventName;
    }

    public String getCategory()
    {
        return category;
    }

    public void setCategory(String category)
    {
        this.category = category;
    }

    public LocalDate getEventDate()
    {
        return eventDate;
    }

    public void setEventDate(LocalDate eventDate)
    {
        this.eventDate = eventDate;
    }

    @JsonProperty("meetName")
    public String getMeetName()
    {
        return meet != null ? meet.getName() : null;
    }

    public Meet getMeet()
    {
        return meet;
    }

    public void setMeet(Meet meet)
    {
        this.meet = meet;
    }

    public String getLocation()
    {
        return location;
    }

    public void setLocation(String location)
    {
        this.location = location;
    }

    public String getImageLink()
    {
        return imageLink;
    }

    public void setImageLink(String imageLink)
    {
        this.imageLink = imageLink;
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