// 📁 service/UserService.java
package com.techyatra.blog_api.service;

import com.techyatra.blog_api.model.User;
import com.techyatra.blog_api.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    
    private final UserRepository repo;

    @Autowired
    public UserService(UserRepository repo) {
        this.repo = repo;
    }

    public User signup(User user) {
        if (repo.findByEmail(user.getEmail()) != null) {
            throw new RuntimeException("Email already registered");
        }
        return repo.save(user);
    }

    public User login(String email, String password) {
        User user = repo.findByEmail(email);
        if (user != null && user.getPassword().equals(password)) {
            return user;
        }
        throw new RuntimeException("Invalid email or password");
    }

    public User getByEmail(String email) {
        return repo.findByEmail(email);
    }
}
