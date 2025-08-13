package com.productpulse.productpulse.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.time.LocalDateTime;

@Entity
@Table(name = "users")
public class Users {
    @Id
    private int id;
    @NotBlank(message = "Username is required")
    @Size(min = 6, max = 50, message = "Username must be between 6 and 50 characters")
    private String username;

    @NotBlank(message = "Password is required")
    @Size(min = 6, message = "Password must be at least 6 characters")
    private String password;

    @NotBlank(message = "Email is required")
    @Email(message = "Invalid email format")
    @Column(unique = true)
    private String email;
    private String otp;
    private LocalDateTime otpGeneratedAt;
    private int otpAttempts;
    private LocalDateTime otpBlockedUntil; 
    private int otpResendCount;
    @NotBlank(message = "Company name is required")
    private String company_name;


    public int getOtpResendCount() {
        return otpResendCount;
    }

    public void setOtpResendCount(int otpResendCount) {
        this.otpResendCount = otpResendCount;
    }

    public LocalDateTime getLastOtpSentAt() {
        return lastOtpSentAt;
    }

    public void setLastOtpSentAt(LocalDateTime lastOtpSentAt) {
        this.lastOtpSentAt = lastOtpSentAt;
    }

    private LocalDateTime lastOtpSentAt;

    public LocalDateTime getOtpGeneratedAt() {
        return otpGeneratedAt;
    }

    public void setOtpGeneratedAt(LocalDateTime otpGeneratedAt) {
        this.otpGeneratedAt = otpGeneratedAt;
    }

    public int getOtpAttempts() {
        return otpAttempts;
    }

    public void setOtpAttempts(int otpAttempts) {
        this.otpAttempts = otpAttempts;
    }

    public LocalDateTime getOtpBlockedUntil() {
        return otpBlockedUntil;
    }

    public void setOtpBlockedUntil(LocalDateTime otpBlockedUntil) {
        this.otpBlockedUntil = otpBlockedUntil;
    }

    public String getOtp() {
        return otp;
    }

    public void setOtp(String otp) {
        this.otp = otp;
    }

    public boolean isVerified() {
        return verified;
    }

    public void setVerified(boolean verified) {
        this.verified = verified;
    }

    private boolean verified;


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCompany_name() {
        return company_name;
    }

    public void setCompany_name(String company_name) {
        this.company_name = company_name;
    }

    @Override
    public String toString() {
        return "Users{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", email='" + email + '\'' +
                ", verified=" + verified +
                '}';
    }

}
