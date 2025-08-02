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
@Builder
@ToString(callSuper = true)
public class Category extends BaseClass {
    
    @Id
    @GeneratedValue
    private UUID id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String description;

    @Lob
    @Column(nullable = false)
    private String imageUrl; // base64 image string
}
