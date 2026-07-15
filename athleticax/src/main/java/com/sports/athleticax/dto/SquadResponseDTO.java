package com.sports.athleticax.dto;

import java.util.List;

public class SquadResponseDTO {

    private List<PlayerDTO> squad;
    private List<PlayerDTO> bestBatsmen;
    private List<PlayerDTO> bestBowlers;
    private List<PlayerDTO> bestAllRounders;
    private List<PlayerDTO> bestWicketkeepers;

    private List<String> warnings;
    private List<String> errors;

    private String captain;
    private String viceCaptain;
    private String explanation;

    public List<PlayerDTO> getSquad() {
        return squad;
    }

    public void setSquad(List<PlayerDTO> squad) {
        this.squad = squad;
    }

    public List<PlayerDTO> getBestBatsmen() {
        return bestBatsmen;
    }

    public void setBestBatsmen(List<PlayerDTO> bestBatsmen) {
        this.bestBatsmen = bestBatsmen;
    }

    public List<PlayerDTO> getBestBowlers() {
        return bestBowlers;
    }

    public void setBestBowlers(List<PlayerDTO> bestBowlers) {
        this.bestBowlers = bestBowlers;
    }

    public List<PlayerDTO> getBestAllRounders() {
        return bestAllRounders;
    }

    public void setBestAllRounders(List<PlayerDTO> bestAllRounders) {
        this.bestAllRounders = bestAllRounders;
    }

    public List<PlayerDTO> getBestWicketkeepers() {
        return bestWicketkeepers;
    }

    public void setBestWicketkeepers(List<PlayerDTO> bestWicketkeepers) {
        this.bestWicketkeepers = bestWicketkeepers;
    }

    public List<String> getWarnings() {
        return warnings;
    }

    public void setWarnings(List<String> warnings) {
        this.warnings = warnings;
    }

    public List<String> getErrors() {
        return errors;
    }

    public void setErrors(List<String> errors) {
        this.errors = errors;
    }

    public String getCaptain() {
        return captain;
    }

    public void setCaptain(String captain) {
        this.captain = captain;
    }

    public String getViceCaptain() {
        return viceCaptain;
    }

    public void setViceCaptain(String viceCaptain) {
        this.viceCaptain = viceCaptain;
    }

    public String getExplanation() {
        return explanation;
    }

    public void setExplanation(String explanation) {
        this.explanation = explanation;
    }
}