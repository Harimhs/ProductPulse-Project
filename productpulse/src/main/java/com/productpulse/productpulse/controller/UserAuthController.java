package com.productpulse.productpulse.controller;

import com.productpulse.productpulse.model.Users;
import com.productpulse.productpulse.payload.ApiResponse;
import com.productpulse.productpulse.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

@RestController
public class UserAuthController {

    @Autowired
    private UserAuthService authService;

    @Autowired
    private OtpService otpService;

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody Users user) {
        System.out.println("Received registration request from: "+ user.getEmail());
        return authService.registerService(user);
    }

    @PostMapping("/register/invite")
    public ResponseEntity<?> registerInvited(@RequestBody Users user){
        System.out.println("Received Invited User registration request from: "+ user.getEmail());
        return authService.registerInviteUserService(user);
    }

    @PostMapping("/api/login")
    public ResponseEntity<Map<String, String>> login(@RequestBody Users user) {
        return authService.loginService(user);
    }

    @GetMapping("/")
    public String home() {
        return "You are not Authenticated!";
    }

    @PostMapping("/verify-otp")
    public CompletableFuture<ResponseEntity<?>> verifyOtp(@RequestParam String email, @RequestParam String otp) {
        System.out.println("Verifying OTP...");
        return otpService.verifyOTPService(email, otp);
    }

    @PostMapping("/resend-otp")
    public CompletableFuture<ResponseEntity<?>> resendOtp(@RequestParam String email) {
        return otpService.resendOtpService(email);
    }

}