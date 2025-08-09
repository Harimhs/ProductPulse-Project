package com.productpulse.productpulse.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.util.Random;

@Service
public class OtpService {

    @Autowired
    private JavaMailSender mailSender;

    public String generateOtp()
    {
        return String.valueOf(new Random().nextInt(899999)+100000);
    }

    public boolean sendOtpEmail(String toEmail, String otp) {
        System.out.println("Sending OTP to: " + toEmail);
        System.out.println("OTP is: " + otp);

        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(toEmail);
            message.setSubject("Your OTP for ProductPulse Registration");
            message.setText("Use this OTP to verify your account: " + otp);
            mailSender.send(message);
            System.out.println("Email sent successfully!");
            return true;
        } catch (Exception e) {
            System.out.println("Email sending failed: " + e.getMessage());
            return false;
        }
    }


}
