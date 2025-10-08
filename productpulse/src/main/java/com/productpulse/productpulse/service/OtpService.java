package com.productpulse.productpulse.service;

import com.productpulse.productpulse.model.Users;
import com.productpulse.productpulse.payload.ApiResponse;
import com.productpulse.productpulse.repo.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Random;
import java.util.concurrent.CompletableFuture;

@Service
public class OtpService {

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private JWTService jwtService;

    public String generateOtp()
    {
        return String.valueOf(new Random().nextInt(899999)+100000);
    }

    @Async
    public void sendOtpEmail(String toEmail, String otp) {
        System.out.println("Sending OTP to: " + toEmail);
        System.out.println("OTP is: " + otp);

        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(toEmail);
            message.setSubject("Your OTP for ProductPulse Registration");
            message.setText("Use this OTP to verify your account: " + otp);
            mailSender.send(message);
            System.out.println("Email sent successfully!");
        } catch (Exception e) {
            System.out.println("Email sending failed: " + e.getMessage());
        }
    }

    @Async
    public CompletableFuture<ResponseEntity<?>> verifyOTPService(String email, String otp) {
        return CompletableFuture.supplyAsync(() -> {
            Optional<Users> optionalUser = userRepo.findByEmail(email);
            if (!optionalUser.isPresent())
                return ResponseEntity.badRequest().body("User not found");

            Users user = optionalUser.get();

            if (user.getOtpGeneratedAt() == null ||
                    user.getOtpGeneratedAt().isBefore(LocalDateTime.now().minusMinutes(10))) {
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
        });
    }


    @Async
    public CompletableFuture<ResponseEntity<?>> resendOtpService(String email) {
        return CompletableFuture.supplyAsync(() -> {
            Optional<Users> optionalUser = userRepo.findByEmail(email);
            if (!optionalUser.isPresent())
                return ResponseEntity.badRequest().body("User not found");

            Users user = optionalUser.get();

            if (user.isVerified())
                return ResponseEntity.badRequest().body("User already verified.");

            if (user.getOtpResendCount() >= 3 && user.getLastOtpSentAt().isAfter(LocalDateTime.now().minusMinutes(15))) {
                return ResponseEntity.status(429).body("OTP resend limit reached. Try again later.");
            }

            if (user.getLastOtpSentAt() != null &&
                    user.getLastOtpSentAt().isAfter(LocalDateTime.now().minusSeconds(60))) {
                return ResponseEntity.status(429).body("Please wait before resending OTP.");
            }

            String newOtp = this.generateOtp();
            user.setOtpHash(newOtp);
            user.setOtpAttempts(0);
            user.setOtpResendCount(user.getOtpResendCount() + 1);
            user.setLastOtpSentAt(LocalDateTime.now());
            userRepo.save(user);

            this.sendOtpEmail(user.getEmail(), newOtp);
            return ResponseEntity.ok("New OTP sent.");
        });
    }


}
