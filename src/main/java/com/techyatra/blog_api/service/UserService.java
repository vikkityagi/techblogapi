// 📁 service/UserService.java
package com.techyatra.blog_api.service;

import com.techyatra.blog_api.dto.OtpResponse;
import com.techyatra.blog_api.dto.UserRequestDto;
import com.techyatra.blog_api.dto.VerifyOtpRequest;

import com.techyatra.blog_api.model.User;
import com.techyatra.blog_api.repository.UserRepository;

import ch.qos.logback.core.joran.util.beans.BeanUtil;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import javax.management.RuntimeErrorException;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final UserRepository repo;
    private final EmailService emailService;
    private static final SecureRandom secureRandom = new SecureRandom();
    private static final Set<String> generatedOtps = new HashSet<>();
    private static final Set<String> generateRefNo = ConcurrentHashMap.newKeySet();

    @Value("${app.security.secret-key}")
    private String secretKey;

    @Autowired
    public UserService(UserRepository repo, EmailService emailService) {
        this.repo = repo;
        this.emailService = emailService;

    }

    public UserRequestDto signup(UserRequestDto userRequestDto) throws Exception {
        User userObj = repo.findByEmail(userRequestDto.getEmail());
        if (userObj != null && userObj.getIsVerify()) {
            throw new Exception("Email already registered");
        } else if (userObj == null || (userObj.getEmail() != null && !userObj.getIsVerify())) {

            if (userObj == null) {
                userObj = new User();
                userObj.setEmail(userRequestDto.getEmail());
                String hashedPassword = hashPasswordSHA512(userRequestDto.getPassword(), secretKey);
                userObj.setPassword(hashedPassword);
                userObj.setRole(userRequestDto.getRole());
            } else {
                String hashedPassword = hashPasswordSHA512(userRequestDto.getPassword(), secretKey);
                if (!hashedPassword.equals(userObj.getPassword())){
                    throw new Exception("Old Password does not match with New Password, please update the password first");
                }else{

                    userObj.setPassword(hashedPassword);
                }
            }

            OtpResponse otpDetails = generateOtpDetails();
            String otp = otpDetails.getOtp();
            // if(userObj != null && userObj.getId() != null &&
            // userObj.getOtp().equals(otp)){
            // otpDetails = generateOtpDetails();
            // otp = otpDetails.getOtp();
            // }
            try {
                emailService.sendOtpEmail(userObj.getEmail(), otp);
            } catch (Exception e) {
                throw new Exception("Could not send OTP: " + e.getMessage());
                // return null;
            }
            generateRefNo.add(otpDetails.getReferenceNumber());
            userRequestDto.setOtpReference(otpDetails.getReferenceNumber());

        } else {
            throw new RuntimeErrorException(null, "Email must not be null");
        }
        User savedUser = repo.save(userObj);
        // UserRequestDto userRequestDto2 = new UserRequestDto();
        BeanUtils.copyProperties(savedUser, userRequestDto);
        userRequestDto.setOtpExpirationTime(emailService.getTime());
        userRequestDto.setIsVerify(userObj.getIsVerify());
        // userRequestDto.setOtpReference(generateRefNo);
        return userRequestDto;
    }

    public VerifyOtpRequest verifyOtp(String email, String otp, String optRef) {
        VerifyOtpRequest request = new VerifyOtpRequest();
        User user = repo.findByEmail(email);
        if (user == null) {
            throw new RuntimeErrorException(null, "User not found with email: " + email);
        }
        boolean isOtpValid = emailService.validateOtp(email, otp);
        if (!isOtpValid) {
            throw new RuntimeErrorException(null, "Invalid or expired OTP.");
        }
        System.out.println("GeneratedRef No: " + generateRefNo + " otpRef: " + optRef);
        if (generateRefNo.contains(optRef)) {
            user.setIsVerify(true);
            // user.setOtp(null);
            repo.save(user);

            request.setIsVerify(true);
            request.setMessage("OTP verified successfully");
            request.setOtp(otp);
            request.setRole(user.getRole());
            request.setEmail(email);

            generateRefNo.remove(optRef);
            return request;
        } else {
            throw new RuntimeErrorException(null, "Invalid OTP or May be Unauthorized User");
        }
    }

    public UserRequestDto login(String email, String password) throws Exception {
        User user = repo.findByEmail(email);
        if (user != null && user.getPassword().equals(password) && user.getEmail().equals(email)) {
            if (user.getIsVerify() == null || !user.getIsVerify().booleanValue()) {
                throw new Exception("User is Not Authorized or Verified");
            }
            OtpResponse otpDetails = generateOtpDetails();
            String otp = otpDetails.getOtp();
            try {
                emailService.sendOtpEmail(user.getEmail(), otp);
            } catch (Exception e) {
                throw new Exception("Could not send OTP: " + e.getMessage());
                // return null;
            }
            generateRefNo.add(otpDetails.getReferenceNumber());
            UserRequestDto dto = new UserRequestDto();
            BeanUtils.copyProperties(user, dto);
            dto.setOtpExpirationTime(emailService.getTime());
            dto.setIsVerify(user.getIsVerify());
            dto.setOtpReference(otpDetails.getReferenceNumber());

            return dto;
        }
        throw new Exception("Invalid email or password");
    }

    public User getByEmail(String email) {
        return repo.findByEmail(email);
    }

    public User reset(User user) {
        User newuser = repo.findByEmail(user.getEmail());
        if (newuser != null && newuser.getEmail().equals(user.getEmail())) {
            String hashedPassword = hashPasswordSHA512(user.getPassword(), secretKey);

            newuser.setPassword(hashedPassword);
            repo.save(newuser);
        } else {
            throw new RuntimeException("Invalid Email");
        }
        return user;
    }

    public static OtpResponse generateOtpDetails() {
        String otp = generateUniqueOtp();
        String referenceNumber = generateReferenceNumber();
        LocalDateTime createdAt = LocalDateTime.now();

        return new OtpResponse(otp, referenceNumber, createdAt);
    }

    private static String generateUniqueOtp() {
        String otp;
        do {
            otp = String.format("%06d", secureRandom.nextInt(1_000_000));
        } while (generatedOtps.contains(otp));

        generatedOtps.add(otp);
        return otp;
    }

    private static String generateReferenceNumber() {
        String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        StringBuilder ref = new StringBuilder();
        for (int i = 0; i < 4; i++) {
            ref.append(chars.charAt(secureRandom.nextInt(chars.length())));
        }
        return ref.toString();
    }

    public static void clearOtps() {
        generatedOtps.clear();
    }

    private String hashPasswordSHA512(String password, String secretKey) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-512");
            byte[] hashedBytes = md.digest((password + secretKey).getBytes(StandardCharsets.UTF_8));

            // Convert byte array to hex string
            StringBuilder sb = new StringBuilder();
            for (byte b : hashedBytes) {
                sb.append(String.format("%02x", b));
            }
            return sb.toString();

        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Error hashing password", e);
        }
    }

}
