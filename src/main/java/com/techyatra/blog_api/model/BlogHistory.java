// 📁 model/BlogHistory.java
package com.techyatra.blog_api.model;

import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonIgnore;
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
public class BlogHistory extends BaseClass {

    @Id
    @GeneratedValue
    private UUID id;

    private String userEmail;

    @ManyToOne
    @JoinColumn(name = "blog_id", nullable = false)
    @JsonIgnoreProperties
    private Blog blog;

    @Column(nullable = false)
    private Boolean isPaid;

    
    // @Column(name = "error_message")
    // private String errorMessage;
}
