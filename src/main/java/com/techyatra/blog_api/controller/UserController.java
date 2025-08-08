// 📁 controller/UserController.java
package com.techyatra.blog_api.controller;

import com.techyatra.blog_api.dto.UserRequestDto;
import com.techyatra.blog_api.dto.VerifyOtpRequest;
import com.techyatra.blog_api.model.User;
import com.techyatra.blog_api.service.UserService;

import jakarta.validation.Valid;

import java.util.Map;

import org.springframework.beans.BeanUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
@CrossOrigin(origins = "*")
public class UserController {

    private final UserService userService;
    

    public UserController(UserService userService) {
        this.userService = userService;
        
    }

    @PostMapping("/signup")
    public ResponseEntity<UserRequestDto> signup(@Valid @RequestBody UserRequestDto userRequestDto) {
        try {
            if (userRequestDto.getEmail() == null || userRequestDto.getPassword() == null || userRequestDto.getRole() == null) {
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
        try{
            if (request.getEmail() == null || request.getOtp() == null) {
                request.setMessage("Email and OTP must not be null");
                return ResponseEntity.badRequest().body("Email and OTP must not be null");
            }
            VerifyOtpRequest verifiedRequest = userService.verifyOtp(request.getEmail(), request.getOtp(), request.getOtpReference());
            if (verifiedRequest.getIsVerify()) {
                return ResponseEntity.ok(verifiedRequest);
            } else {
                request.setMessage(("OTP verification failed"));
                return ResponseEntity.badRequest().body("OTP verification failed");
            }
        }catch (Exception e) {
            // request.setMessage(null);
            return ResponseEntity.badRequest().body("Error processing OTP verification: " + e.getMessage());
        }
        

        
    }

    @PostMapping("/login")
    public ResponseEntity<User> login(@RequestBody User user) throws Exception {
        try {
            if (user.getEmail() == null || user.getPassword() == null) {
                user.setErrorMessage("Email and password must not be null");
                return new ResponseEntity<>(user, HttpStatus.BAD_REQUEST);
            }
            User savedUser = userService.login(user.getEmail(), user.getPassword());
            if (savedUser == null || savedUser.getId() == null || savedUser.getEmail() == null) {
                user.setErrorMessage("Invalid email or password");
                // Return a 401 Unauthorized status if login fails
                return new ResponseEntity<>(user, HttpStatus.UNAUTHORIZED);

            }
            return new ResponseEntity<>(savedUser, HttpStatus.OK);
        } catch (Exception e) {
            user.setErrorMessage(e.getMessage());
            return new ResponseEntity<>(user, HttpStatus.BAD_REQUEST);
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
}
