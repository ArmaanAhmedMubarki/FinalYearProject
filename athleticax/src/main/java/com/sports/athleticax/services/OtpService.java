package com.sports.athleticax.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class OtpService {

    private final Map<String, OtpData> otpStorage = new ConcurrentHashMap<>();

    private static final int OTP_VALID_SECONDS = 300;

    @Autowired
    private JavaMailSender mailSender;

    public void sendOtp(String email) {

        if (email == null || email.isEmpty()) {
            throw new RuntimeException("Email cannot be null");
        }

        OtpData existing = otpStorage.get(email);

        // ❌ Block resend if not expired
        if (existing != null && LocalDateTime.now().isBefore(existing.expiryTime)) {
            throw new RuntimeException("OTP still valid. Please wait before requesting a new one.");
        }

        String otp = String.valueOf(new Random().nextInt(900000) + 100000);

        LocalDateTime expiry = LocalDateTime.now().plusSeconds(OTP_VALID_SECONDS);

        otpStorage.put(email, new OtpData(otp, expiry));

        // ✅ SEND EMAIL
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(email);
            message.setSubject("AthleticaX OTP Verification");
            message.setText("Your OTP is: " + otp + "\nValid for 5 minutes.");

            mailSender.send(message);

            System.out.println("OTP sent to email: " + email + " OTP=" + otp);

        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to send OTP email");
        }
    }

    public boolean verifyOtp(String email, String otp) {

        OtpData data = otpStorage.get(email);

        if (data == null) return false;

        if (LocalDateTime.now().isAfter(data.expiryTime)) {
            otpStorage.remove(email);
            return false;
        }

        boolean valid = data.otp.equals(otp);

        if (valid) {
            otpStorage.remove(email);
        }

        return valid;
    }

    static class OtpData {
        String otp;
        LocalDateTime expiryTime;

        public OtpData(String otp, LocalDateTime expiryTime) {
            this.otp = otp;
            this.expiryTime = expiryTime;
        }
    }
}