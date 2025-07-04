// 📁 model/User.java
package com.techyatra.blog_api.model;

import java.util.UUID;

import com.techyatra.blog_api.auditclass.BaseClass;

import jakarta.persistence.*;
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

    @Column(unique = true,nullable = false)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String role; // "admin" or "user"
}
