package com.techyatra.blog_api.dto;

import lombok.Data;

// VerifyOtpRequest.java
@Data
public class VerifyOtpRequest {
    private String email;
    private String otp;
    private String message;
    private Boolean isVerify;
    private String otpReference;
}
