package com.productpulse.productpulse.controller;

import com.productpulse.productpulse.model.Users;
import com.productpulse.productpulse.payload.ApiResponse;
import com.productpulse.productpulse.service.JWTService;
import com.productpulse.productpulse.service.OtpService;
import com.productpulse.productpulse.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.productpulse.productpulse.repo.UserRepo;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
public class TestController {

    @Autowired
    private UserService service;

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private OtpService otpService;

    @Autowired
    private JWTService jwtService;

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody Users user) {
        try {
            Optional<Users> optionalUser = userRepo.findByEmail(user.getEmail());
            if (optionalUser.isPresent())
                return ResponseEntity.badRequest().body("Email already registered. Please verify or login.");

            String otp = otpService.generateOtp();
            user.setOtpHash(otp);
            user.setVerified(false);
            user.setOtpGeneratedAt(LocalDateTime.now());
            user.setOtpAttempts(0);
            user.setOtpBlockedUntil(null);
            user.setOtpResendCount(0);
            user.setLastOtpSentAt(LocalDateTime.now());

            try {
                otpService.sendOtpEmail(user.getEmail(), otp);
            } catch (Exception e) {
                return ResponseEntity.status(500).body("Email could not be sent: " + e.getMessage());
            }
            try {
                service.register(user);
            } catch (Exception e) {
                return ResponseEntity.status(500).body("Failed to save user: " + e.getMessage());
            }
            return ResponseEntity.ok(new ApiResponse<>("Registration successful. Please check your email for OTP.", null));
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Unexpected error: " + e.getMessage());
        }
    }

    @PostMapping("/api/login")
    public ResponseEntity<Map<String, String>> login(@RequestBody Users user) {
        String token = service.verify(user);
        Map<String, String> response = new HashMap<>();
        response.put("token", token);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/")
    public String home() {
        return "You are Authenticated!";
    }

    @PostMapping("/verify-otp")
    public ResponseEntity<?> verifyOtp(@RequestParam String email, @RequestParam String otp) {
        Optional<Users> optionalUser = userRepo.findByEmail(email);
        if (!optionalUser.isPresent()) return ResponseEntity.badRequest().body("User not found");

        Users user = optionalUser.get();

        if (user.getOtpGeneratedAt() == null || user.getOtpGeneratedAt().isBefore(LocalDateTime.now().minusMinutes(10))) {
            return ResponseEntity.status(401).body("OTP expired. Please request a new one.");
        }

        if (user.getOtpBlockedUntil() != null && user.getOtpBlockedUntil().isAfter(LocalDateTime.now())) {
            return ResponseEntity.status(429).body("Too many attempts. Try after " + user.getOtpBlockedUntil());
        }

        if (!otp.equals(user.getOtpHash())) {
            user.setOtpAttempts(user.getOtpAttempts() + 1);
            if (user.getOtpAttempts() >= 5) {
                user.setOtpBlockedUntil(LocalDateTime.now().plusMinutes(10));
                userRepo.save(user);
                return ResponseEntity.status(429).body("Too many attempts. Try again after 10 minutes.");
            }
            userRepo.save(user);
            return ResponseEntity.status(401).body("Invalid OTP. Attempts left: " + (5 - user.getOtpAttempts()));
        }

        user.setVerified(true);
        user.setOtpHash(null);
        user.setOtpAttempts(0);
        user.setOtpBlockedUntil(null);
        userRepo.save(user);

        String token = jwtService.generateToken(user.getEmail());
        System.out.println("Generated token for " + user.getEmail() + ": " + token);
        return ResponseEntity.ok(new ApiResponse<>("OTP verified, login successful!", token));

    }


    @PostMapping("/resend-otp")
    public ResponseEntity<?> resendOtp(@RequestParam String email) {
        Optional<Users> optionalUser = userRepo.findByEmail(email);
        if (!optionalUser.isPresent()) return ResponseEntity.badRequest().body("User not found");

        Users user = optionalUser.get();

        if (user == null) return ResponseEntity.badRequest().body("User not found");

        if (user.isVerified()) return ResponseEntity.badRequest().body("User already verified.");

        if (user.getOtpResendCount() >= 3 && user.getLastOtpSentAt().isAfter(LocalDateTime.now().minusMinutes(15))) {
            return ResponseEntity.status(429).body("OTP resend limit reached. Try again later.");
        }

        if (user.getLastOtpSentAt() != null &&
                user.getLastOtpSentAt().isAfter(LocalDateTime.now().minusSeconds(60))) {
            return ResponseEntity.status(429).body("Please wait before resending OTP.");
        }

        String newOtp = otpService.generateOtp();
        user.setOtpHash(newOtp);
        user.setOtpAttempts(0);
        user.setOtpResendCount(user.getOtpResendCount() + 1);
        user.setLastOtpSentAt(LocalDateTime.now());
        userRepo.save(user);

        otpService.sendOtpEmail(user.getEmail(), newOtp);
        return ResponseEntity.ok("New OTP sent.");
    }
}