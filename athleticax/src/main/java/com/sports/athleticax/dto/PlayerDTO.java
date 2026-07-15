package com.sports.athleticax.dto;

public class PlayerDTO {

    private String name;
    private String role;
    private double selectionProbability;

    public PlayerDTO() {
    }

    public PlayerDTO(String name, String role, double selectionProbability) {
        this.name = name;
        this.role = role;
        this.selectionProbability = selectionProbability;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public double getSelectionProbability() {
        return selectionProbability;
    }

    public void setSelectionProbability(double selectionProbability) {
        this.selectionProbability = selectionProbability;
    }
}