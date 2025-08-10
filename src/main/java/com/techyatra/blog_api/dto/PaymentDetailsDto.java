package com.techyatra.blog_api.dto;

import lombok.Data;

@Data
public class PaymentDetailsDto {
    private String qrBase64; // QR code as Base64 string
    private String accountHolder;
    private String bankName;
    private String accountNumber;
    private String ifsc;
    private String branch;
    private String txnId; // Generated transaction ID
    // getters & setters
}