package com.techyatra.blog_api.service;

import java.util.UUID;

import org.springframework.stereotype.Service;

import com.techyatra.blog_api.dto.PaymentDetailsDto;
import com.techyatra.blog_api.dto.TransactionStatusDto;

@Service
public class PaymentService {

    public PaymentDetailsDto getPaymentDetails() {
        // Fetch from DB or config
        PaymentDetailsDto dto = new PaymentDetailsDto();
        dto.setQrBase64(generateQrBase64("upi://pay?pa=vikki@upi&am=100"));
        dto.setAccountHolder("VIKKI S/O SUBHASH");
        dto.setBankName("Punjab National Bank");
        dto.setAccountNumber("********5422"); // mask for safety
        dto.setIfsc("PUNB0180100");
        dto.setBranch("Faloda, Muzaffarnagar, UP");
        dto.setTxnId(UUID.randomUUID().toString());
        return dto;
    }

    private String generateQrBase64(String upiLink) {
        // Generate QR code and return as Base64 (ZXing library)
        return "...base64string...";
    }

    public TransactionStatusDto getTransactionStatus(String txnId) {
        // Call payment gateway or DB to check transaction progress
        TransactionStatusDto status = new TransactionStatusDto();
        status.setTxnId(txnId);
        status.setStatus("PENDING");
        return status;
    }
}
