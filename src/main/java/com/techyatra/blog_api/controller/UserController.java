// 📁 controller/UserController.java
package com.techyatra.blog_api.controller;

import com.techyatra.blog_api.model.User;
import com.techyatra.blog_api.service.UserService;

import jakarta.validation.Valid;

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
    public ResponseEntity<User> signup(@Valid @RequestBody User user) {
        try {
            if (user.getEmail() == null || user.getPassword() == null || user.getRole() == null) {
                user.setErrorMessage("Email, password, and role must not be null");
                return new ResponseEntity<>(user, HttpStatus.BAD_REQUEST);
            }
            User savedUser = userService.signup(user);
            if (savedUser == null || savedUser.getId() == null || savedUser.getEmail() == null) {
                user.setErrorMessage("User could not be saved");
                return new ResponseEntity<>(user, HttpStatus.BAD_REQUEST);
            }
            return new ResponseEntity<>(savedUser, HttpStatus.CREATED);
        } catch (Exception e) {
            user.setErrorMessage(e.getMessage());
            return new ResponseEntity<>(user, HttpStatus.INTERNAL_SERVER_ERROR);
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
