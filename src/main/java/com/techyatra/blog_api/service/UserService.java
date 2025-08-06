// 📁 service/UserService.java
package com.techyatra.blog_api.service;

import com.techyatra.blog_api.model.User;
import com.techyatra.blog_api.repository.UserRepository;

import javax.management.RuntimeErrorException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    
    private final UserRepository repo;

    @Autowired
    public UserService(UserRepository repo) {
        this.repo = repo;
    }

    public User signup(User user) throws Exception {
            if (repo.findByEmail(user.getEmail()) != null) {
                throw new Exception("Email already registered");
        }
        User savedUser = repo.save(user);
        return savedUser;
    }

    public User login(String email, String password) throws Exception {
        User user = repo.findByEmail(email);
        if (user != null && user.getPassword().equals(password) && user.getEmail().equals(email)) {
            return user;
        }
        throw new Exception("Invalid email or password");
    }

    public User getByEmail(String email) {
        return repo.findByEmail(email);
    }

    public User reset(User user){
        User newuser = repo.findByEmail(user.getEmail());
        if(newuser != null && newuser.getEmail().equals(user.getEmail())){
            newuser.setPassword(user.getPassword());
            repo.save(newuser);
        }else{
            throw new RuntimeException("Invalid Email");
        }
        return user;
    }
}
