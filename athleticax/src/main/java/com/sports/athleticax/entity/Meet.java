package com.sports.athleticax.entity;

import jakarta.persistence.*;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.LocalDate;

@Entity
@Table(name = "meets")
public class Meet
{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(nullable = false, unique = true)
    @JsonProperty("name")
    private String name;

    @Column(nullable = false)
    @JsonProperty("startDate")
    private LocalDate startDate;

    @Column(nullable = false)
    @JsonProperty("endDate")
    private LocalDate endDate;


    public long getId()
    {
        return id;
    }

    public void setId(long id)
    {
        this.id = id;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public LocalDate getStartDate()
    {
        return startDate;
    }

    public void setStartDate(LocalDate startDate)
    {
        this.startDate = startDate;
    }

    public LocalDate getEndDate()
    {
        return endDate;
    }

    public void setEndDate(LocalDate endDate)
    {
        this.endDate = endDate;
    }

    public String getStatus()
    {
        LocalDate today = LocalDate.now();

        if (today.isBefore(startDate))
        {
            return "UPCOMING";
        }
        else if (today.isAfter(endDate))
        {
            return "COMPLETED";
        }
        else
        {
            return "ONGOING";
        }
    }
}