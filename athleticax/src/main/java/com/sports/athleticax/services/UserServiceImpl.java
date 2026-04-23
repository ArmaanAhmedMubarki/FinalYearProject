package com.sports.athleticax.services;

import com.sports.athleticax.dto.LoginRequest;
import com.sports.athleticax.entity.*;
import com.sports.athleticax.dto.RegisterRequest;
import com.sports.athleticax.exception.ApiException;
import com.sports.athleticax.repository.RoleRepository;
import com.sports.athleticax.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public User registerUser(RegisterRequest request) {

        if (userRepository.findByEmail(request.getEmail()) != null) { // Check for duplicate email
            throw new ApiException("This email is already registered");
        }

        User user = new User();
        user.setName(request.getName());
        user.setEmail(request.getEmail()); // Set email
        user.setPassword(passwordEncoder.encode(request.getPassword()));

        System.out.println(request.getRole());


        // Determine role based on input
        Role userRole = roleRepository.findByName(request.getRole().toUpperCase());
        if (userRole == null) {
            throw new ApiException("Role not found");
        }

        // Special handling for ADMIN role
        if ("ADMIN".equalsIgnoreCase(request.getRole())) {
            // Create and populate the Admin entity
            Admin admin = new Admin();
            admin.setUser(user); // Link admin to the user
            // Link admin to user
            user.setAdmin(admin);
        }

        //Special handling for ATHLETE role
        if ("ATHLETE".equalsIgnoreCase(request.getRole())) {
            // Create and populate the ATHLETE entity
            Athlete athlete = new Athlete();
            athlete.setUser(user); // Link admin to the user
            // Link admin to user
            user.setAthlete(athlete);
        }

        //Special handling for COACH role
        if ("COACH".equalsIgnoreCase(request.getRole())) {
            // Create and populate the ATHLETE entity
            Coach coach = new Coach();
            coach.setUser(user); // Link admin to the user
            // Link admin to user
            user.setCoach(coach);
        }

        user.getRoles().add(userRole);


        return userRepository.save(user);
    }

    @Override
    public User findUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    public User authenticateUser(LoginRequest request) {
        // Find the user by username
        User user = userRepository.findByEmail(request.getEmail());

        if (user == null) {
            throw new ApiException("User not found");
        }

        // Compare the plaintext password from the request with the hashed password stored in the database
        boolean passwordMatches = passwordEncoder.matches(request.getPassword(), user.getPassword());


        if (!passwordMatches) {
            throw new ApiException("Invalid username or password");
        }

        // Return the user if authentication is successful
        return user;
    }

}
