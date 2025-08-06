// 📁 model/User.java
package com.techyatra.blog_api.model;

import java.util.UUID;

import com.techyatra.blog_api.auditclass.BaseClass;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true)
@Table(name = "users")
public class User extends BaseClass {

    @Id
    @GeneratedValue
    private UUID id;

    @Column(unique = true, nullable = false)
    @NotBlank(message = "Email is mandatory")
    @Email(message = "Invalid email format")
    @Pattern(regexp = "^[a-zA-Z0-9._%+-]+@[a-z0-9.-]+\\.[a-z]{2,4}$", message = "Invalid email pattern")
    private String email;

    @Column(nullable = false)
    @NotBlank(message = "Password is mandatory")
    @Size(min = 6, message = "Password must be at least 6 characters")
    @Pattern(regexp = "^(?=.*[A-Z])(?=.*[0-9])(?=.*[!@#$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>/?]).{6,}$", message = "Password must contain uppercase, number, and special character")
    private String password;

    @Column(nullable = false)
    @NotBlank(message = "Role is required")
    @Pattern(regexp = "^(admin|user)$", message = "Role must be 'admin' or 'user'")
    private String role;

    @Column(name = "error_message")
    private String errorMessage;

    @PrePersist
    @PreUpdate
    private void sanitizeInput() {
        this.email = this.email.replaceAll("<[^>]*>", "");
        this.password = this.password.replaceAll("<[^>]*>", "");
        this.role = this.role.replaceAll("<[^>]*>", "");
    }
}
