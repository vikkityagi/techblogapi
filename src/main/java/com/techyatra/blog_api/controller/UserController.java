// 📁 controller/UserController.java
package com.techyatra.blog_api.controller;

import com.techyatra.blog_api.dto.UserRequestDto;
import com.techyatra.blog_api.dto.VerifyOtpRequest;
import com.techyatra.blog_api.model.User;
import com.techyatra.blog_api.repository.UserRepository;
import com.techyatra.blog_api.service.UserService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Map;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
@CrossOrigin(origins = "*")
public class UserController {

    private final UserService userService;
    private final UserRepository repo;
    @Value("${app.security.secret-key}")
    private String secretKey;

    public UserController(UserService userService, UserRepository userRepository) {
        this.userService = userService;
        this.repo = userRepository;

    }

    @PostMapping("/signup")
    public ResponseEntity<UserRequestDto> signup(@Valid @RequestBody UserRequestDto userRequestDto) {
        try {
            if (userRequestDto.getEmail() == null || userRequestDto.getPassword() == null
                    || userRequestDto.getRole() == null) {
                userRequestDto.setErrorMessage("Email, password, and role must not be null");
                return new ResponseEntity<>(userRequestDto, HttpStatus.BAD_REQUEST);
            }
            UserRequestDto savedUserDto = userService.signup(userRequestDto);
            if (savedUserDto == null || savedUserDto.getId() == null || savedUserDto.getEmail() == null) {
                userRequestDto.setErrorMessage("User could not be saved");
                return new ResponseEntity<>(userRequestDto, HttpStatus.BAD_REQUEST);
            }
            // BeanUtils.copyProperties(userRequestDto, savedUser);
            // userRequestDto.set
            return new ResponseEntity<>(savedUserDto, HttpStatus.CREATED);
        } catch (Exception e) {
            userRequestDto.setErrorMessage(e.getMessage());
            return new ResponseEntity<>(userRequestDto, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // UserController.java
    @PostMapping("/verify-otp")
    public ResponseEntity<?> verifyOtp(@RequestBody VerifyOtpRequest request) {
        try {
            if (request.getEmail() == null || request.getOtp() == null) {
                request.setMessage("Email and OTP must not be null");
                return ResponseEntity.badRequest().body("Email and OTP must not be null");
            }
            VerifyOtpRequest verifiedRequest = userService.verifyOtp(request.getEmail(), request.getOtp(),
                    request.getOtpReference());
            if (verifiedRequest.getIsVerify()) {
                return ResponseEntity.ok(verifiedRequest);
            } else {
                request.setMessage(("OTP verification failed"));
                return ResponseEntity.badRequest().body("OTP verification failed");
            }
        } catch (Exception e) {
            // request.setMessage(null);
            return ResponseEntity.badRequest().body("Error processing OTP verification: " + e.getMessage());
        }

    }

    @PostMapping("/login")
    public ResponseEntity<UserRequestDto> login(@Valid @RequestBody UserRequestDto userRequestDto) throws Exception {
        try {
            if (userRequestDto.getEmail() == null || userRequestDto.getPassword() == null) {
                userRequestDto.setErrorMessage("Email and password must not be null");
                return new ResponseEntity<>(userRequestDto, HttpStatus.BAD_REQUEST);
            }
            UserRequestDto savedUser = userService.login(userRequestDto.getEmail(), userRequestDto.getPassword());
            if (savedUser == null || savedUser.getId() == null || savedUser.getEmail() == null) {
                userRequestDto.setErrorMessage("Invalid email or password");
                // Return a 401 Unauthorized status if login fails
                return new ResponseEntity<>(userRequestDto, HttpStatus.UNAUTHORIZED);

            }
            return new ResponseEntity<>(savedUser, HttpStatus.OK);
        } catch (Exception e) {
            userRequestDto.setErrorMessage(e.getMessage());
            return new ResponseEntity<>(userRequestDto, HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/{email}")
    public User getByEmail(@PathVariable String email) {
        return userService.getByEmail(email);
    }

    @PostMapping("/reset")
    public ResponseEntity<User> resetPassword(@RequestBody User user) throws Exception {
        try {
            if (user.getEmail() == null || user.getPassword() == null) {
                throw new IllegalArgumentException("Email and password must not be null");
            }
            User resetUser = userService.reset(user);
            if (resetUser == null) {
                user.setErrorMessage("Password reset failed");
                return new ResponseEntity<>(user, HttpStatus.BAD_REQUEST);
            }
            return new ResponseEntity<>(resetUser, HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            user.setErrorMessage(e.getMessage());
            return new ResponseEntity<>(user, HttpStatus.BAD_REQUEST);
        }

    }

    @PostMapping("/admin/create")
    public ResponseEntity<?> createAdmin(@Valid @RequestBody UserRequestDto req, HttpServletRequest request) {
        String ip = request.getHeader("X-Forwarded-For");
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        if (!"10.49.22.86".equals(ip)) { // your real static IP
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("IP not allowed");
        }

        // String certFingerprint = (String) request.getAttribute("javax.servlet.request.X509CertificateFingerprint");
        // if (!"YOUR_CERT_FINGERPRINT".equals(certFingerprint)) {
        //     return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Certificate not allowed");
        // }

        User admin = new User();
        String hashedPassword = hashPasswordSHA512(req.getPassword(), secretKey);
        admin.setEmail(req.getEmail());
        admin.setPassword(hashedPassword);
        admin.setRole("ADMIN");
        admin.setIsVerify(true);
        repo.save(admin);

        return ResponseEntity.ok("Admin created");
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
