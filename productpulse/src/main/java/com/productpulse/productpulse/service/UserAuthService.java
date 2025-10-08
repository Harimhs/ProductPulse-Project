package com.productpulse.productpulse.service;

import com.productpulse.productpulse.DTO.PartialUser;
import com.productpulse.productpulse.controller.TeamInviteController;
import com.productpulse.productpulse.model.Users;
import com.productpulse.productpulse.payload.ApiResponse;
import com.productpulse.productpulse.repo.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
public class UserAuthService {

    private BCryptPasswordEncoder bcode= new BCryptPasswordEncoder(12);

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private OtpService otpService;

    @Autowired
    private JWTService jwtService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JWTService JWTService;

    @Autowired
    private TeamInviteController teamInviteController;

    @Async
    public void sendOtpAsync(String email, String otp) {
        otpService.sendOtpEmail(email, otp);
    }

    public ResponseEntity<?> registerService(@RequestBody Users user) {

        try {
            Optional<Users> optionalUser = userRepo.findByEmail(user.getEmail());
            if (optionalUser.isPresent()) {
                return ResponseEntity.badRequest().body("Email already registered. Please verify or login.");
            }

            String otp = otpService.generateOtp();
            user.setOtpHash(otp);
            user.setVerified(false);
            user.setOtpGeneratedAt(LocalDateTime.now());
            user.setOtpAttempts(0);
            user.setOtpBlockedUntil(null);
            user.setOtpResendCount(0);
            user.setLastOtpSentAt(LocalDateTime.now());

            try {
                sendOtpAsync(user.getEmail(), otp);
            } catch (Exception e) {
                return ResponseEntity.status(500).body("Email could not be sent: " + e.getMessage());
            }

            try {
                user.setPassword(bcode.encode(user.getPassword()));
                userRepo.save(user);
            } catch (Exception e) {
                return ResponseEntity.status(500).body("Failed to save user: " + e.getMessage());
            }

            return ResponseEntity.ok(new ApiResponse<>("Registration successful. Please check your email for OTP.", null));
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Unexpected error: " + e.getMessage());
        }
    }

    public ResponseEntity<?> registerInviteUserService(@RequestBody Users user){
        try {
            ResponseEntity<?> response = teamInviteController.acceptInvite(user.getInviteToken());
            if (!response.getStatusCode().is2xxSuccessful() || response.getBody() == null) {
                return ResponseEntity.badRequest().body("Invite not valid");
            }

            PartialUser inviteUserData= (PartialUser) response.getBody();

            user.setCompanyId(inviteUserData.getCompanyId());
            user.setRole(inviteUserData.getRole());
            user.setEmail(inviteUserData.getEmail());

            try {
                Optional<Users> optionalUser = userRepo.findByEmail(user.getEmail());
                if (optionalUser.isPresent()) {
                    return ResponseEntity.badRequest().body("Email already registered. Please verify or login.");
                }
            } catch (Exception e) {
                return ResponseEntity.status(500).body("Failed to save user: " + e.getMessage());
            }

            try {
                user.setPassword(bcode.encode(user.getPassword()));
                userRepo.save(user);
            } catch (Exception e) {
                return ResponseEntity.status(500).body("Failed to save user: " + e.getMessage());
            }

            user.setVerified(true);
            userRepo.save(user);

            String token = jwtService.generateToken(user.getEmail());
            System.out.println("Generated token for " + user.getEmail() + ": " + token);

            return ResponseEntity.ok(new ApiResponse<>("Registration successful for invited user.", token));

        } catch (Exception e) {
            return ResponseEntity.status(500).body("Unexpected error: " + e.getMessage());
        }
    }

    public ResponseEntity<Map<String, String>> loginService(Users user){
        try{
            Authentication authentication =
                    authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                            user.getEmail(), user.getPassword()));
            String token= jwtService.generateToken(user.getEmail());
            Map<String, String> response = new HashMap<>();
            response.put("message", "Login successful");
            response.put("token", token);
            return ResponseEntity.ok(response);
        } catch (BadCredentialsException e) {
            return ResponseEntity.status(401).body(Map.of(
                    "error", "Invalid email or password"
            ));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(Map.of(
                    "error", "An unexpected error occurred: " + e.getMessage()
            ));
        }
    }

}
