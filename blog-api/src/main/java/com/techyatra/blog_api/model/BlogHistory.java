// 📁 model/BlogHistory.java
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
public class BlogHistory extends BaseClass {

    @Id
    @GeneratedValue
    private UUID id;

    private String userEmail;

    @OneToOne
    private Blog blog;

    @Column(nullable = false)
    private Boolean isPaid;
}
