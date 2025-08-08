package com.techyatra.blog_api.dto;

import java.time.LocalDateTime;

public class OtpResponse {
    private String otp;
    private String referenceNumber;
    private LocalDateTime createdAt;

    public OtpResponse(String otp, String referenceNumber, LocalDateTime createdAt) {
        this.otp = otp;
        this.referenceNumber = referenceNumber;
        this.createdAt = createdAt;
    }

    // Getters
    public String getOtp() {
        return otp;
    }

    public String getReferenceNumber() {
        return referenceNumber;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    @Override
    public String toString() {
        return "OtpResponse{" +
                "otp='" + otp + '\'' +
                ", referenceNumber='" + referenceNumber + '\'' +
                ", createdAt=" + createdAt +
                '}';
    }
}
