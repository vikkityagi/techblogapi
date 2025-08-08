package com.techyatra.blog_api.service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    private final Map<String, String> otpStorage = new ConcurrentHashMap<>();
    private final Map<String, Long> otpExpiry = new ConcurrentHashMap<>();

    private static final long EXPIRY_DURATION = 5 * 60 * 1000; // 5 minutes
    

    public void sendOtpEmail(String toEmail, String otp) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(toEmail);
        message.setSubject("TechYatra Blog API - OTP Verification");
        message.setText("Your OTP is: " + otp + "\nPlease use this OTP to verify your email and don't share this OTP with anyone.");
        mailSender.send(message);

        otpStorage.put(toEmail,otp);
        otpExpiry.put(toEmail, System.currentTimeMillis() + EXPIRY_DURATION);
    }

    

    

    public boolean validateOtp(String email, String otp) {
        if (!otpStorage.containsKey(email)) return false;

        long expiryTime = otpExpiry.get(email);
        if (System.currentTimeMillis() > expiryTime) {
            otpStorage.remove(email);
            otpExpiry.remove(email);
            return false;
        }

        if (otpStorage.get(email).equals(otp)) {
            otpStorage.remove(email);
            otpExpiry.remove(email);
            return true;
        }
        return false;
    }

    public long getTime(){
        return this.EXPIRY_DURATION;
    }
}
