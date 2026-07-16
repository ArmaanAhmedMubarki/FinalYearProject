package com.sports.athleticax.controller;

import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;

import com.sports.athleticax.services.OtpService;
import com.sports.athleticax.services.UserService;
import com.sports.athleticax.dto.*;
import com.sports.athleticax.entity.*;
import com.sports.athleticax.repository.AdminRepository;
import com.sports.athleticax.security.JwtTokenProvider;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private UserService userService;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @Autowired
    private OtpService otpService;

    @Autowired
    private AdminRepository adminRepository;

    // ================= OTP =================

    @PostMapping("/send-otp")
    public ResponseEntity<?> sendOtp(@RequestBody RegisterRequest request) {

        if (request.getEmail() == null || request.getEmail().isEmpty()) {
            return ResponseEntity.badRequest().body("Email is required");
        }

        if (userService.emailExists(request.getEmail())) {
            return ResponseEntity.badRequest().body("Email already registered");
        }

        try {
            otpService.sendOtp(request.getEmail());
            return ResponseEntity.ok("OTP sent successfully");
        } catch (RuntimeException e) {
            // Returns an HTTP 400 or 500 with the exact error string instead of a blank crash
            return ResponseEntity.status(500).body(e.getMessage());
        }
    }

    // ✅ RESEND OTP
    @PostMapping("/resend-otp")
    public ResponseEntity<?> resendOtp(@RequestBody RegisterRequest request) {

        if (request.getEmail() == null || request.getEmail().isEmpty()) {
            return ResponseEntity.badRequest().body("Email is required");
        }

        if (userService.emailExists(request.getEmail())) {
            return ResponseEntity.badRequest().body("Email already registered");
        }

        otpService.sendOtp(request.getEmail());

        return ResponseEntity.ok("OTP resent successfully");
    }

    // ✅ CHECK IF ADMIN EXISTS
    @GetMapping("/check-admin-exists")
    public ResponseEntity<?> checkAdminExists() {
        boolean adminExists = !adminRepository.findAll().isEmpty();
        return ResponseEntity.ok(new java.util.HashMap<String, Boolean>() {
            {
                put("adminExists", adminExists);
            }
        });
    }

    @PostMapping("/register-with-otp")
    public ResponseEntity<?> registerWithOtp(
            @RequestBody RegisterRequest request,
            @RequestParam String otp) {

        if (request.getEmail() == null || otp == null || otp.isEmpty()) {
            return ResponseEntity.badRequest().body("Email and OTP are required");
        }

        boolean valid = otpService.verifyOtp(request.getEmail(), otp);

        if (!valid) {
            return ResponseEntity.badRequest().body("Invalid or expired OTP");
        }
        if (!otpService.isOtpVerified(request.getEmail())) {
        return ResponseEntity.badRequest().body("Unauthorized OTP flow");
        }
        // ADMIN restriction
        if ("ADMIN".equalsIgnoreCase(request.getRole()) && userService.adminExists()) {
        return ResponseEntity.badRequest().body("Admin already exists");
        }

        User user = userService.registerUser(request);
        otpService.clearOtp(request.getEmail());

        return ResponseEntity.ok(user);
    }

    // ================= LOGIN =================

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {

        User user = userService.authenticateUser(request);

        String token = jwtTokenProvider.generateToken(user.getEmail());

        //  Safe role extraction
        String role = user.getRoles() != null && !user.getRoles().isEmpty()
                ? user.getRoles().iterator().next().getName()
                : "UNKNOWN";

        Long roleId = 0L;

        try {
            if ("ADMIN".equalsIgnoreCase(role) && user.getAdmin() != null) {
                roleId = user.getAdmin().getId();
            } else if ("ATHLETE".equalsIgnoreCase(role) && user.getAthlete() != null) {
                roleId = user.getAthlete().getId();
            } else if ("COACH".equalsIgnoreCase(role) && user.getCoach() != null) {
                roleId = user.getCoach().getId();
            }
        } catch (Exception e) {
            // fallback safety
            roleId = 0L;
        }

        return ResponseEntity.ok(
                new AuthResponse(token, "Login successful", role, roleId)
        );
    }
    // ================= FORGOT PASSWORD =================

   @PostMapping("/forgot-password")
   public ResponseEntity<?> forgotPassword(@RequestParam String email) {

    if (!userService.emailExists(email)) {
        return ResponseEntity.badRequest().body("Email not registered");
    }

    otpService.sendOtp(email);

    return ResponseEntity.ok("OTP sent to email");
   }


   @PostMapping("/verify-otp-reset")
   public ResponseEntity<?> verifyOtpReset(
        @RequestParam String email,
        @RequestParam String otp) {

    boolean valid = otpService.verifyOtp(email, otp);

    if (!valid) {
        return ResponseEntity.badRequest().body("Invalid or expired OTP");
    }

    return ResponseEntity.ok("OTP verified");
   }

   @PostMapping("/reset-password")
   public ResponseEntity<?> resetPassword(@RequestBody ResetPasswordRequestDTO request) {

    if (!request.getPassword().equals(request.getConfirmPassword())) {
        return ResponseEntity.badRequest().body("Passwords do not match");
    }

    userService.updatePassword(request.getEmail(), request.getPassword());

    return ResponseEntity.ok("Password updated successfully");
   }
}
