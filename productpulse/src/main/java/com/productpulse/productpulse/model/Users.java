package com.productpulse.productpulse.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import java.time.LocalDateTime;

@Entity
@Table(name = "users")
public class Users {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @NotBlank(message = "Username is required")
    @Size(min = 6, max = 50, message = "Username must be between 6 and 50 characters")
    @Column(nullable = false, length = 50)
    private String username;

    @NotBlank(message = "Password is required")
    @Size(min = 6, message = "Password must be at least 6 characters")
    @Column(nullable = false)
    private String password;

    @Column(name = "otp_hash")
    private String otpHash;

    private boolean verified;

    @NotBlank(message = "Email is required")
    @Email(message = "Invalid email format")
    @Column(nullable = false, unique = true)
    private String email;

    @Column(name = "otp_attempts", nullable = false)
    private int otpAttempts;

    @Column(name = "otp_generated_at")
    private LocalDateTime otpGeneratedAt;

    @Column(name = "otp_blocked_until")
    private LocalDateTime otpBlockedUntil;

    @Column(name = "last_otp_sent_at")
    private LocalDateTime lastOtpSentAt;

    @Column(name = "otp_resend_count", nullable = false)
    private int otpResendCount;

    @Column(name = "company_id")
    private Long companyId;

    @Column(length = 50)
    private String role;

    @Column(name = "created_at", updatable = false, insertable = false)
    private LocalDateTime createdAt;

    // Getters and setters...

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public String getOtpHash() { return otpHash; }
    public void setOtpHash(String otpHash) { this.otpHash = otpHash; }

    public boolean isVerified() { return verified; }
    public void setVerified(boolean verified) { this.verified = verified; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    
    public int getOtpAttempts() { return otpAttempts; }
    public void setOtpAttempts(int otpAttempts) { this.otpAttempts = otpAttempts; }

    public LocalDateTime getOtpGeneratedAt() { return otpGeneratedAt; }
    public void setOtpGeneratedAt(LocalDateTime otpGeneratedAt) { this.otpGeneratedAt = otpGeneratedAt; }

    public LocalDateTime getOtpBlockedUntil() { return otpBlockedUntil; }
    public void setOtpBlockedUntil(LocalDateTime otpBlockedUntil) { this.otpBlockedUntil = otpBlockedUntil; }

    public LocalDateTime getLastOtpSentAt() { return lastOtpSentAt; }
    public void setLastOtpSentAt(LocalDateTime lastOtpSentAt) { this.lastOtpSentAt = lastOtpSentAt; }

    public int getOtpResendCount() { return otpResendCount; }
    public void setOtpResendCount(int otpResendCount) { this.otpResendCount = otpResendCount; }

    public Long getCompanyId() { return companyId; }
    public void setCompanyId(Long companyId) { this.companyId = companyId; }

    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    @Override
    public String toString() {
        return "Users{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", email='" + email + '\'' +
                ", verified=" + verified +
                ", role='" + role + '\'' +
                ", companyId=" + companyId +
                '}';
    }
}
