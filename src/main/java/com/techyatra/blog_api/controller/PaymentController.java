package com.techyatra.blog_api.controller;

import java.security.Principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.techyatra.blog_api.dto.PaymentDetailsDto;
import com.techyatra.blog_api.dto.TransactionStatusDto;
import com.techyatra.blog_api.service.PaymentService;

@RestController
@RequestMapping("/api/payments")
@CrossOrigin
public class PaymentController {

    @Autowired
    private PaymentService paymentService;

    @GetMapping("/details")
    public ResponseEntity<PaymentDetailsDto> getPaymentDetails() {
        // You could verify the logged-in user here
        PaymentDetailsDto details = paymentService.getPaymentDetails();
        return ResponseEntity.ok(details);
    }

    @GetMapping("/status/{txnId}")
    public ResponseEntity<TransactionStatusDto> getTransactionStatus(@PathVariable String txnId) {
        TransactionStatusDto status = paymentService.getTransactionStatus(txnId);
        return ResponseEntity.ok(status);
    }
}
