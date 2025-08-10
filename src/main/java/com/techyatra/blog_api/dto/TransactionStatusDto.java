package com.techyatra.blog_api.dto;

import lombok.Data;

@Data
public class TransactionStatusDto {
    private String txnId;
    private String status; // "PENDING", "SUCCESS", "FAILED"
    // getters & setters
}
