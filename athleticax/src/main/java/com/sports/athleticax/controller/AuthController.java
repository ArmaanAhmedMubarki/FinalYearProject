package com.sports.athleticax.controller;

import com.sports.athleticax.dto.AuthResponse;
import com.sports.athleticax.dto.LoginRequest;
import com.sports.athleticax.dto.RegisterRequest;
import com.sports.athleticax.entity.*;
import com.sports.athleticax.services.UserService;
import com.sports.athleticax.security.JwtTokenProvider; // Import JWT utility class
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private UserService userService;

    @Autowired
    private JwtTokenProvider jwtTokenProvider; // Inject JWT utility

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequest request) {
        User user = userService.registerUser(request);
        return ResponseEntity.ok(user);
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        // Authenticate the user
        User user = userService.authenticateUser(request);

        // Generate JWT token
        String token = jwtTokenProvider.generateToken(user.getEmail());

        // Create a response message
        String message = "Login successful for user: " + user.getName();

        String role = user.getRoles().stream()
                .map(Role::getName)
                .findFirst()
                .orElse("UNKNOWN");
        Long roleId = 0L;
        if ("ADMIN".equalsIgnoreCase(role)){
            Admin admin = user.getAdmin();
            roleId = admin.getId();
        }else if ("ATHLETE".equalsIgnoreCase(role)){
            Athlete athlete = user.getAthlete();
            roleId = athlete.getId();
        }else  if ("COACH".equalsIgnoreCase(role)){
            Coach coach = user.getCoach();
            roleId = coach.getId();
        }
        else{
            System.out.println("No role type found");
        }
        // Return token and message in response
        return ResponseEntity.ok(new AuthResponse(token, message, role, roleId)); // Create an AuthResponse class to hold the token and message
    }
}
