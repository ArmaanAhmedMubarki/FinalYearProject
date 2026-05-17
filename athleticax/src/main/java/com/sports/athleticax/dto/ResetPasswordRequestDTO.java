package com.sports.athleticax.dto;

public class ResetPasswordRequestDTO {

    private String email;
    private String password;
    private String confirmPassword;

    // Default Constructor (required for JSON mapping)
    public ResetPasswordRequestDTO() {
    }

    // Parameterized Constructor
    public ResetPasswordRequestDTO(String email, String password, String confirmPassword) {
        this.email = email;
        this.password = password;
        this.confirmPassword = confirmPassword;
    }

    // Getter and Setter for email
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    // Getter and Setter for password
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    // Getter and Setter for confirmPassword
    public String getConfirmPassword() {
        return confirmPassword;
    }

    public void setConfirmPassword(String confirmPassword) {
        this.confirmPassword = confirmPassword;
    }
}