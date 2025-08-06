package com.techyatra.blog_api.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.techyatra.blog_api.auditclass.BaseClass;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true)
public class Blog extends BaseClass {

    @Id
    @GeneratedValue
    private UUID id;

    @Column(unique = true, nullable = false)
    private String title;

    
    @Column(length = 5000,nullable = false)
    private String content;

    @Column(nullable = false)
    private LocalDateTime date = LocalDateTime.now();

    @Column(nullable = false)
    private String userEmail;

    @Column(nullable = false, columnDefinition = "boolean default false")
    private boolean paid;

    @Column(nullable = false)
    private String titleNumber;
    
    @Column(name = "category_id", nullable = false)
    private UUID category;

    // @Column(name = "error_message")
    // private String errorMessage;

    
}
