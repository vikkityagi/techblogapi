// 📁 repository/UserRepository.java
package com.techyatra.blog_api.repository;

import com.techyatra.blog_api.model.User;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, UUID> {
    User findByEmail(String email);
}
