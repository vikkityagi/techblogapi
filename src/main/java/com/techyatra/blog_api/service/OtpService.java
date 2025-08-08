// package com.techyatra.blog_api.service;

// import java.util.Map;
// import java.util.Random;
// import java.util.concurrent.ConcurrentHashMap;

// import org.springframework.stereotype.Service;

// @Service
// public class OtpService {

//     private final Map<String, String> otpStorage = new ConcurrentHashMap<>();
//     private final Map<String, Long> otpExpiry = new ConcurrentHashMap<>();

//     private static final long EXPIRY_DURATION = 5 * 60 * 1000; // 5 minutes

//     public String generateOtp(String email) {
//         String otp = String.format("%06d", new Random().nextInt(999999));
//         otpStorage.put(email, otp);
//         otpExpiry.put(email, System.currentTimeMillis() + EXPIRY_DURATION);
//         return otp;
//     }

//     public boolean validateOtp(String email, String otp) {
//         if (!otpStorage.containsKey(email)) return false;

//         long expiryTime = otpExpiry.get(email);
//         if (System.currentTimeMillis() > expiryTime) {
//             otpStorage.remove(email);
//             otpExpiry.remove(email);
//             return false;
//         }

//         if (otpStorage.get(email).equals(otp)) {
//             otpStorage.remove(email);
//             otpExpiry.remove(email);
//             return true;
//         }
//         return false;
//     }
// }
